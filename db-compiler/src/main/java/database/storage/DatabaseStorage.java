package database.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.metadata.DataType;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;
import database.utils.Pair;

public class DatabaseStorage {
	private static final FileOutputStreamLRUCache<File, Pair<Integer, FileOutputStream>> insertCache = new FileOutputStreamLRUCache<>(10);
	
	public static void insertRecord(File file, ITableDef tableDef, Map<IColumnDef, String> columnValue) {
		byte[] buffer = getRecordBuffer(tableDef);

		List<IColumnDef> columns = tableDef.getColumns();
		writeRecord(buffer, columns, columnValue);

		File data = getTableDatFile(file);
		persistRecord(data, buffer);

		tableDef.incrementRowsCount();

		DefStorage.updateRowsCount(file, tableDef);
	}

	public static Result getRecords(File table, ITableDef tableDef) {
		try {
			Pair<Integer, FileOutputStream> pair = insertCache.get(table);
			if(pair != null){
				pair.getSnd().flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		int recordSize = getRecordSize(tableDef);

		int recordsCount = tableDef.getRowsCount();

		int bufferSize = recordSize * recordsCount;
		byte[] buffer = new byte[bufferSize];

		File data = getTableDatFile(table);
		try (FileInputStream input = new FileInputStream(data)) {
			input.read(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Result(buffer, recordSize, createMapOffset(tableDef));
	}

	private static void persistRecord(File table, byte[] recordBuffer) {

		try {
			Pair<Integer, FileOutputStream> pair = insertCache.get(table);
			if (pair == null) {
				pair = new Pair<Integer, FileOutputStream>(0, new FileOutputStream(table, true));
				insertCache.put(table, pair);
			}
			
			int count = pair.getFst();
			FileOutputStream out = pair.getSnd();

			out.write(recordBuffer);
			if (count % 50 == 0) {
				out.flush();
			}
			
			count++;

			insertCache.put(table, new Pair<Integer, FileOutputStream>(count, out));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static byte[] getRecordBuffer(ITableDef tableDef) {
		return new byte[getRecordSize(tableDef)];
	}

	private static void writeRecord(byte[] buffer, List<IColumnDef> columns,
			Map<IColumnDef, String> columnValue) {
		for (int i = 0, offset = 0; i < columns.size(); i++) {
			IColumnDef column = columns.get(i);

			int size = getColumnSize(column);
			if (columnValue.containsKey(column)) {
				String value = columnValue.get(column);

				DataType type = column.getDataType();
				if (type == DataType.INTEGER) {
					int intValue = Integer.parseInt(value);
					DataUtils.writeInt(intValue, buffer, offset);
				} else {
					String strValue = value;
					DataUtils.writeString(strValue, size, buffer, offset);
				}

			}
			offset += size;
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

		throw new RuntimeException(String.format(
				"DataType inválido para a coluna %s", columnDef.getName()));
	}

	private static Map<IColumnDef, Pair<Integer, Integer>> createMapOffset(
			ITableDef tableDef) {
		Map<IColumnDef, Pair<Integer, Integer>> result = new HashMap<>();

		List<IColumnDef> columns = tableDef.getColumns();
		for (int i = 0, offset = 0; i < columns.size(); i++) {
			IColumnDef def = columns.get(i);

			int columnSize = getColumnSize(def);
			result.put(def, new Pair<Integer, Integer>(offset, columnSize));
			offset += columnSize;
		}

		return result;
	}

	private static File getTableDatFile(File file) {
		for (File inner : file.listFiles()) {
			if (inner.getName().endsWith(".dat")) {
				return inner;
			}
		}
		throw new RuntimeException(String.format(
				"TableDef não encontrado para a tabela %s", file.getName()));
	}

}
