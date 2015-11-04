package database.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import database.metadata.ColumnDef;
import database.metadata.DataType;
import database.metadata.TableDef;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;

public class DefStorage {

    /*
     * 90 bytes name 4 bytes rowsCount 1 byte columnsCount
     */
    private static final int TABLE_DEF_SIZE = 95;

    /*
     * 90 bytes name 4 bytes capacity 1 byte data type
     */
    private static final int COLUMN_DEF_SIZE = 95;

    public static ITableDef getTableDef(File table) {
        File def = getTableDefFile(table);

        return readTableDef(def);
    }

    public static void setTableDef(File table, ITableDef tableDef) {
        File def = getTableDefFile(table);

        byte[] buffer = getTableDefBuffer(tableDef);

        try (FileOutputStream out = new FileOutputStream(def, false)) {
            out.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateRowsCount(File table, ITableDef tableDef) {
        File def = getTableDefFile(table);

        int rowsCount = tableDef.getRowsCount();
        byte[] byteInt = DataUtils.toByteArray(rowsCount);

        try (RandomAccessFile out = new RandomAccessFile(def, "rw")) {
            out.seek(90);
            out.write(byteInt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getTableDefBuffer(ITableDef tableDef) {
        byte[] buffer = new byte[TABLE_DEF_SIZE];

        String name = tableDef.getName();
        DataUtils.writeString(name, 90, buffer, 0);

        int rowsCount = tableDef.getRowsCount();
        DataUtils.writeInt(rowsCount, buffer, 90);

        buffer[94] = (byte) tableDef.getColumnsCount();

        List<IColumnDef> columnsDef = tableDef.getColumns();
        byte[] columnsBuffer = getColumnsDefBuffer(columnsDef);

        return DataUtils.join(buffer, columnsBuffer);
    }

    private static byte[] getColumnsDefBuffer(List<IColumnDef> columnsDef) {
        int coulmnsCount = columnsDef.size();

        byte[] buffer = new byte[coulmnsCount * COLUMN_DEF_SIZE];

        for (int i = 0, offset = 0; i < coulmnsCount; i++) {
            IColumnDef columnDef = columnsDef.get(i);

            String name = columnDef.getName();
            DataUtils.writeString(name, 90, buffer, offset);
            offset += 90;

            int capacity = columnDef.getCapacity();
            DataUtils.writeInt(capacity, buffer, offset);
            offset += 4;

            byte dataTypeIndex = (byte) columnDef.getDataType().ordinal();
            buffer[offset] = dataTypeIndex;
            offset += 1;
        }

        return buffer;
    }

    private static ITableDef readTableDef(File def) {

        byte[] buffer = new byte[TABLE_DEF_SIZE];

        try (FileInputStream fi = new FileInputStream(def)) {
            fi.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String name = DataUtils.readString(buffer, 0, 90);
        int rowsCount = DataUtils.readInt(buffer, 90);
        byte columnsCount = buffer[94];

        IColumnDef[] columnsDef = readColumnDef(def, columnsCount);

        TableDef tableDef = new TableDef(name, rowsCount, columnsDef);

        return tableDef;
    }

    private static IColumnDef[] readColumnDef(File def, int columnsCount) {
        int bufferSize = columnsCount * COLUMN_DEF_SIZE;
        byte[] buffer = new byte[bufferSize];

        try (FileInputStream fi = new FileInputStream(def)) {
            fi.skip(TABLE_DEF_SIZE);
            fi.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ColumnDef[] columnsDef = new ColumnDef[columnsCount];
        for (int i = 0, offset = 0; i < columnsCount; i++) {

            String name = DataUtils.readString(buffer, offset, 90);
            offset += 90;

            int capacity = DataUtils.readInt(buffer, offset);
            offset += 4;

            DataType dataType = DataType.values()[buffer[offset]];
            offset += 1;

            ColumnDef columnDef = new ColumnDef(name, dataType, capacity);
            columnsDef[i] = columnDef;
        }

        return columnsDef;
    }

    private static File getTableDefFile(File file) {
        for (File inner : file.listFiles()) {
            if (inner.getName().endsWith(".def")) {
                return inner;
            }
        }
        throw new RuntimeException(String.format("TableDef não encontrado para a tabela %s", file.getName()));
    }

}
