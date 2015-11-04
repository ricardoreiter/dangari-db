/*
 * Created on 16/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.metadata;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.storage.DatabaseStorage;

/**
 * Esta é a implementação concreta, que terá ligação com o sistema de arquivos
 * 
 * @author ricardo.reiter
 */
public class DatabaseDef implements IDatabaseDef {

    private Map<ITableDef, File> tables = new LinkedHashMap<>();
    private Map<String, ITableDef> tablesByName = new LinkedHashMap<>();

    @Override
    public ITableDef getTableDef(String tableName) {
        return tablesByName.get(tableName);
    }

    @Override
    public ITableDef createTable(String name) {
        return createTable(name, new IColumnDef[0]);
    }

    @Override
    public ITableDef createTable(String name, IColumnDef... columnsDef) {
        TableDef tableDef = new TableDef(name, columnsDef);
        return tableDef;
    }

    @Override
    public IColumnDef createColumnDef(String name, DataType dataType, int capacity) {
        return new ColumnDef(name, dataType, capacity);
    }

    public void addTable(ITableDef tableDef, File tableFile) {
        tables.put(tableDef, tableFile);
        tablesByName.put(tableDef.getName(), tableDef);
    }

    public void insert(ITableDef tableDef, Map<IColumnDef, Object> values) {
        File file = tables.get(tableDef);

        if (file == null) {
            throw new RuntimeException(String.format("Não foi encontrada a tabela %s", tableDef.getName()));
        }

        DatabaseStorage.insertRecord(file, tableDef, values);
    }

    @Override
    public Map<String, ITableDef> getTables() {
        return tablesByName;
    }

}
