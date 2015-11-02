package database.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public ITableDef getTableDef(File table) {
		File def = getTableDefFile(table);

		return readTableDef(def);
	}

	public void setTableDef(File table, ITableDef tableDef) {
		File def = getTableDefFile(table);

		writeTableDef(def, tableDef);
	}

	private void writeTableDef(File def, ITableDef tableDef) {
		byte[] buffer = new byte[TABLE_DEF_SIZE];

		try (FileOutputStream out = new FileOutputStream(def, false)) {
			out.write(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<IColumnDef> columnsDef = tableDef.getColumns();
		writeColumnsDef(def, columnsDef);
	}

	private void writeColumnsDef(File def, List<IColumnDef> columnsDef) {
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

		try (FileOutputStream out = new FileOutputStream(def, false)) {
			out.write(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private ITableDef readTableDef(File def) {

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

	private IColumnDef[] readColumnDef(File def, int columnsCount) {
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

	private File getTableDefFile(File file) {
		for (File inner : file.listFiles()) {
			if (inner.getName().endsWith(".def")) {
				return inner;
			}
		}
		throw new RuntimeException(String.format("TableDef não encontrado para a tabela %s", file.getName()));
	}

}
