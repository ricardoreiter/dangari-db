package database.metadata.interfaces;

import java.util.List;

public interface ITableDeff {

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

	IColumnDeff getColumnDeff(String column);

	void addColumnDeff(IColumnDeff column);

	List<IColumnDeff> getColumns();

	void deleteColumnDeff(String name);
}
