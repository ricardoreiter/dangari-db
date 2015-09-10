package database.manager;

import database.metadata.ColumnDeff;
import database.metadata.DataType;
import database.metadata.TableDeff;
import database.metadata.interfaces.IColumnDeff;
import database.metadata.interfaces.ITableDeff;

public final class DatabaseManager {

	private final static DatabaseManager INSTANCE = new DatabaseManager();

	private DatabaseManager() {
	}

	public static DatabaseManager getInstance() {
		return INSTANCE;
	}

	public static ITableDeff createTable(String name) {
		return new TableDeff(name);
	}

	public static ITableDeff createTable(String name, IColumnDeff... columnsDeff) {
		return new TableDeff(name, columnsDeff);
	}

	public static IColumnDeff createColumnDeff(String name, DataType dataType, Number maxValue) {
		return new ColumnDeff(name, dataType, maxValue);
	}

}
