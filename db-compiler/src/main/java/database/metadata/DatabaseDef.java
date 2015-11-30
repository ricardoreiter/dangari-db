/*
 * Created on 16/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.storage.DatabaseStorage;
import database.storage.DefStorage;
import database.storage.Result;
import database.utils.JoinUtils;
import database.utils.JoinUtils.IRegistry;

/**
 * Esta é a implementação concreta, que terá ligação com o sistema de arquivos
 * 
 * @author ricardo.reiter
 */
public class DatabaseDef implements IDatabaseDef {

    private String name;
    private Map<ITableDef, File> tables = new LinkedHashMap<>();
    private Map<String, ITableDef> tablesByName = new LinkedHashMap<>();

    public DatabaseDef(String name) {
        this.name = name;
    }

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

    @Override
    public void addTable(ITableDef tableDef, File tableFile) {
        tables.put(tableDef, tableFile);
        tablesByName.put(tableDef.getName(), tableDef);
    }

    @Override
    public void insert(ITableDef tableDef, Map<IColumnDef, String> values) {
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

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<IRegistry> getRecords(ITableDef tableDef) {
        File tableFile = tables.get(tableDef);

        Result recordsResult = DatabaseStorage.getRecords(tableFile, tableDef);
        List<IRegistry> listRegistrys = new ArrayList<JoinUtils.IRegistry>(tableDef.getRowsCount());
        for (int i = 0; i < tableDef.getRowsCount(); i++) {
            IRegistry newRegistry = new IRegistry();

            for (IColumnDef column : tableDef.getColumns()) {
                newRegistry.columnValue.put(column, recordsResult.getAsObject(column));
            }

            listRegistrys.add(newRegistry);
            recordsResult.next();
        }
        return listRegistrys;
    }

    @Override
    public void removeTable(ITableDef tableDef) {
        File fileTable = tables.get(tableDef);
        for (File file : fileTable.listFiles()) {
            file.delete();
        }
        fileTable.delete();
        tables.remove(tableDef);
        tablesByName.remove(tableDef.getName());
    }

    @Override
    public Index createIndex(ITableDef tableDef, IColumnDef column) {
        File fileTable = tables.get(tableDef);
        DefStorage.createIndex(fileTable, tableDef, column);
        return DefStorage.getIndex(fileTable, column);
    }

}
