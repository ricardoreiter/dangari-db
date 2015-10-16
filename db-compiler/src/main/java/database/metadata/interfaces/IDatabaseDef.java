/*
 * Created on 07/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.metadata.interfaces;

import database.metadata.DataType;

/**
 * @author ricardo.reiter
 */
public interface IDatabaseDef {

    ITableDef getTableDef(String tableName);

    ITableDef createTable(String name);

    ITableDef createTable(String name, IColumnDef... columnsDeff);

    IColumnDef createColumnDef(String name, DataType dataType, Number maxValue);

}
