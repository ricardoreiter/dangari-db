/*
 * Created on 16/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.metadata;

import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

/**
 * Esta é a implementação concreta, que terá ligação com o sistema de arquivos
 * 
 * @author ricardo.reiter
 */
public class DatabaseDef implements IDatabaseDef {

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#getTableDef(java.lang.String)
     */
    @Override
    public ITableDef getTableDef(String tableName) {
        return null;
    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#createTable(java.lang.String)
     */
    @Override
    public ITableDef createTable(String name) {
        return null;
    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#createTable(java.lang.String, database.metadata.interfaces.IColumnDef[])
     */
    @Override
    public ITableDef createTable(String name, IColumnDef... columnsDeff) {
        return null;
    }

    /* (non-Javadoc)
     * @see database.metadata.interfaces.IDatabaseDef#createColumnDef(java.lang.String, database.metadata.DataType, java.lang.Number)
     */
    @Override
    public IColumnDef createColumnDef(String name, DataType dataType, Number maxValue) {
        return null;
    }

}
