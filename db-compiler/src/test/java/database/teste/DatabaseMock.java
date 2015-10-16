/*
 * Created on 07/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.util.HashMap;
import java.util.Map;

import database.metadata.ColumnDef;
import database.metadata.DataType;
import database.metadata.TableDef;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

/**
 * Mock para testes do compilador e executor
 * 
 * @author ricardo.reiter
 */
public class DatabaseMock implements IDatabaseDef {

    private Map<String, ITableDef> tables = new HashMap<>();

    public DatabaseMock() {
        ITableDef table = createTable("usuario");
        IColumnDef column = createColumnDef("cod", DataType.NUMBER, 100);
        table.addColumnDef(column);

        column = createColumnDef("nome", DataType.VARCHAR, 100);
        table.addColumnDef(column);

        table = createTable("empresa");
        column = createColumnDef("cod", DataType.NUMBER, 100);
        table.addColumnDef(column);

        column = createColumnDef("nome", DataType.VARCHAR, 100);
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
    public IColumnDef createColumnDef(String name, DataType dataType, Number maxValue) {
        return new ColumnDef(name, dataType, maxValue);
    }

}
