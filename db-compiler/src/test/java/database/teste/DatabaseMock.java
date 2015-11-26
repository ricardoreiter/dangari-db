/*
 * Created on 07/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.metadata.ColumnDef;
import database.metadata.DataType;
import database.metadata.Index;
import database.metadata.TableDef;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.utils.JoinUtils.IRegistry;

/**
 * Mock para testes do compilador e executor
 * 
 * @author ricardo.reiter
 */
public class DatabaseMock implements IDatabaseDef {

    private Map<String, ITableDef> tables = new HashMap<>();

    public DatabaseMock() {
        ITableDef table = createTable("usuario");
        IColumnDef column = createColumnDef("cod", DataType.INTEGER, 100);
        table.addColumnDef(column);

        column = createColumnDef("nome", DataType.VARCHAR, 100);
        table.addColumnDef(column);

        column = createColumnDef("caractere", DataType.CHAR, 100);
        table.addColumnDef(column);

        table = createTable("empresa");
        column = createColumnDef("cod", DataType.INTEGER, 100);
        table.addColumnDef(column);

        column = createColumnDef("nome", DataType.VARCHAR, 100);
        table.addColumnDef(column);

        column = createColumnDef("caractere", DataType.CHAR, 100);
        table.addColumnDef(column);
    }

    @Override
    public ITableDef getTableDef(String tableName) {
        return tables.get(tableName);
    }

    @Override
    public ITableDef createTable(String name) {
        TableDef table = new TableDef(name);
        tables.put(name, table);
        return table;
    }

    @Override
    public ITableDef createTable(String name, IColumnDef... columnsDeff) {
        return new TableDef(name, columnsDeff);
    }

    @Override
    public IColumnDef createColumnDef(String name, DataType dataType, int maxValue) {
        return new ColumnDef(name, dataType, maxValue);
    }

    @Override
    public Map<String, ITableDef> getTables() {
        return null;
    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#addTable(database.metadata.interfaces.ITableDef, java.io.File)
     */
    @Override
    public void addTable(ITableDef tableDef, File tableFile) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#insert(database.metadata.interfaces.ITableDef, java.util.Map)
     */
    @Override
    public void insert(ITableDef tableDef, Map<IColumnDef, String> values) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<IRegistry> getRecords(ITableDef tableDef) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#removeTable(database.metadata.interfaces.ITableDef)
     */
    @Override
    public void removeTable(ITableDef tableDef) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#createIndex(database.metadata.interfaces.ITableDef, database.metadata.interfaces.IColumnDef)
     */
    @Override
    public Index createIndex(ITableDef tableDef, IColumnDef column) {
        return new Index();
    }

}
