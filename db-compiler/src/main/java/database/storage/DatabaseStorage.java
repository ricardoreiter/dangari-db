package database.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import database.metadata.ColumnDef;
import database.metadata.DataType;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;

public class DatabaseStorage {

	public static void insertRecord(File file, ITableDef tableDef, Map<ColumnDef, Object> columnValue) {
		byte[] buffer = getRecordBuffer(tableDef);

		List<IColumnDef> columns = tableDef.getColumns();
		writeRecord(buffer, columns, columnValue);

		persistRecord(file, buffer);
	}

	private static void persistRecord(File table, byte[] recordBuffer) {
		try (FileOutputStream out = new FileOutputStream(table, true)) {
			out.write(recordBuffer);
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	private static byte[] getRecordBuffer(ITableDef tableDef) {
		return new byte[getRecordSize(tableDef)];
	}

	private static void writeRecord(byte[] buffer, List<IColumnDef> columns, Map<ColumnDef, Object> columnValue) {
		for (int i = 0, offset = 0; i < columns.size(); i++) {
			IColumnDef column = columns.get(i);

			int size = getColumnSize(column);
			if (columnValue.containsKey(column)) {
				Object value = columnValue.get(column);

				DataType type = column.getDataType();
				if (type == DataType.INTEGER) {
					int intValue = (int) value;
					DataUtils.writeInt(intValue, buffer, offset);
				} else {
					String strValue = String.valueOf(value);
					DataUtils.writeString(strValue, size, buffer, offset);
				}

				offset += size;
			}
		}
	}

	private static int getRecordSize(ITableDef tableDef) {
		int size = 0;
		List<IColumnDef> columns = tableDef.getColumns();

		for (IColumnDef iColumnDef : columns) {
			int columnSize = getColumnSize(iColumnDef);
			size += columnSize;
		}

		return size;
	}

	private static int getColumnSize(IColumnDef columnDef) {
		DataType dataType = columnDef.getDataType();

		if (dataType == DataType.INTEGER) {
			return 4;
		} else if (dataType == DataType.CHAR || dataType == DataType.VARCHAR) {
			return columnDef.getCapacity() * DataUtils.STR_BYTES;
		}

		throw new RuntimeException(String.format("DataType inválido para a coluna %s", columnDef.getName()));
	}

}
