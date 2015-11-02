/*
 * Created on 16/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.metadata;

import java.io.File;
import java.util.HashMap;
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

	private Map<ITableDef, File> tables = new HashMap<>();

	@Override
	public ITableDef getTableDef(String tableName) {
		return null;
	}

	@Override
	public ITableDef createTable(String name) {
		return null;
	}

	@Override
	public ITableDef createTable(String name, IColumnDef... columnsDeff) {
		return null;
	}

	@Override
	public IColumnDef createColumnDef(String name, DataType dataType, int maxValue) {
		return null;
	}

	public void insert(ITableDef tableDef, Map<ColumnDef, Object> values) {
		File file = tables.get(tableDef);

		if (file == null) {
			throw new RuntimeException(String.format("Não foi encontrada a tabela %s", tableDef.getName()));
		}

		DatabaseStorage.insertRecord(file, tableDef, values);
	}

}
