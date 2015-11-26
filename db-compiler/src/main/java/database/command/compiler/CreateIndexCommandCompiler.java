/*
 * Created on 05/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.command.compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import database.command.CreateIndexCommandExecutor;
import database.command.ICommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

/**
 * @author ricardo.reiter
 */
public class CreateIndexCommandCompiler implements ICommandCompiler {

    private String indexId;
    private String tableName;
    private ArrayList<String> columns = new ArrayList<>();

    @Override
    public void accept(int action, Token token) {
        switch (action) {
            case 22:
                indexId = token.getLexeme();
                break;
            case 23:
                tableName = token.getLexeme();
                break;
            case 25:
                columns.add(token.getLexeme());
                break;
            default:
                break;
        }
    }

    @Override
    public ICommandExecutor compile() throws SemanticError {
        IDatabaseDef database = DatabaseManager.INSTANCE.getActualDatabase();
        if (database == null) {
            throw new SemanticError("Não há database ativo, use [SET DATABASE NOME] para ativar um database");
        }
        ITableDef tableDef = database.getTableDef(tableName);
        if (tableDef == null) {
            throw new SemanticError(String.format("Tabela com o nome [%s], não existe", tableName));
        }

        List<IColumnDef> checkedColumns = new LinkedList<>();
        for (String column : columns) {
            IColumnDef columnDef = tableDef.getColumnDef(column);
            if (columnDef == null) {
                throw new SemanticError(String.format("Coluna [%s] não existente", column));
            }
            if (checkedColumns.contains(columnDef)) {
                throw new SemanticError(String.format("Coluna [%s] já declarada", column));
            }
            if (tableDef.getIndex(columnDef) != null) {
                throw new SemanticError(String.format("Coluna [%s] já possuí índice", column));
            }
            checkedColumns.add(columnDef);
        }
        return new CreateIndexCommandExecutor(tableDef, checkedColumns);
    }

}
