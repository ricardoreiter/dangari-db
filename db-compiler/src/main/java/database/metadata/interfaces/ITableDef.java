package database.metadata.interfaces;

import java.util.List;

import database.metadata.Index;

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

    Index getIndex(IColumnDef columnDef);

    void addIndex(IColumnDef columnDef, Index index);

    void deleteIndex(IColumnDef columnDef);

}
