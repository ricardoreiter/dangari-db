package database.metadata.interfaces;

import java.util.List;

public interface ITableDef {

	String getName();

	void setName(String name);

	int getColumnsCount();

	int getRowsCount();

	/**
	 * rowsCount++
	 */
	void incrementRowsCount();

	/**
	 * rowsCount--
	 */
	void decrementRowsCount();

	IColumnDef getColumnDef(String column);

	void addColumnDef(IColumnDef column);

	List<IColumnDef> getColumns();

	void deleteColumnDef(String name);

}
