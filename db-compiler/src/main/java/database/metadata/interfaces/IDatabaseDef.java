/*
 * Created on 07/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.metadata.interfaces;

import java.io.File;
import java.util.Map;

import database.metadata.DataType;

/**
 * @author ricardo.reiter
 */
public interface IDatabaseDef {

    String getName();

    ITableDef getTableDef(String tableName);

    ITableDef createTable(String name);

    ITableDef createTable(String name, IColumnDef... columnsDeff);

    IColumnDef createColumnDef(String name, DataType dataType, int capacity);

    Map<String, ITableDef> getTables();

    /**
     * @param tableDef
     * @param tableFile
     */
    void addTable(ITableDef tableDef, File tableFile);

    void insert(ITableDef tableDef, Map<IColumnDef, String> values);

}
