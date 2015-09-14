package database.manager;

import database.metadata.ColumnDef;
import database.metadata.DataType;
import database.metadata.TableDef;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;

public final class DatabaseManager {

	private final static DatabaseManager INSTANCE = new DatabaseManager();

	private DatabaseManager() {
	}

	public static DatabaseManager getInstance() {
		return INSTANCE;
	}

	public static ITableDef createTable(String name) {
		return new TableDef(name);
	}

	public static ITableDef createTable(String name, IColumnDef... columnsDeff) {
		return new TableDef(name, columnsDeff);
	}

	public static IColumnDef createColumnDeff(String name, DataType dataType, Number maxValue) {
		return new ColumnDef(name, dataType, maxValue);
	}

}
