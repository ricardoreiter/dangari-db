/*
 * Created on 05/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.command.compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import database.command.CreateTableCommandExecutor;
import database.command.ICommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;
import database.metadata.DataType;
import database.metadata.interfaces.IDatabaseDef;

/**
 * @author ricardo.reiter
 */
public class CreateTableCommandCompiler implements ICommandCompiler {

    private String tableName;
    private ArrayList<CompilerColumn> columns = new ArrayList<>();
    private CompilerColumn actualColumn;

    public class CompilerColumn {

        public String columnName;
        public DataType type;
        public float capacity; //na hora de compilar, atualmente ele pode botar ponto, mas dps quando for fazer a verificação semantica não deve deixar
    }

    @Override
    public void accept(int action, Token token) {
        switch (action) {
            case 0:
                actualColumn.type = DataType.INTEGER;
                break;
            case 1:
                actualColumn.type = DataType.CHAR;
                break;
            case 2:
                actualColumn.type = DataType.VARCHAR;
                break;
            case 3:
                actualColumn.capacity = Float.parseFloat(token.getLexeme());
                break;
            case 21:
                this.tableName = token.getLexeme();
                break;
            case 25:
                actualColumn = new CompilerColumn();
                actualColumn.columnName = token.getLexeme();
                columns.add(actualColumn);
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
        if (database.getTableDef(tableName) != null) {
            throw new SemanticError(String.format("Tabela com o nome [%s], já existe no database ativo", tableName));
        }

        List<String> checkedColumnNames = new LinkedList<>();
        for (CompilerColumn column : columns) {
            if (checkedColumnNames.contains(column.columnName)) {
                throw new SemanticError(String.format("Coluna [%s] já declarada", column.columnName));
            }
            if (column.capacity != Math.ceil(column.capacity)) {
                throw new SemanticError(String.format("Coluna [%s] do tipo [%s], só pode ser declarada com um tamanho do tipo Inteiro", column.columnName, column.type));
            }
            checkedColumnNames.add(column.columnName);
        }
        return new CreateTableCommandExecutor(tableName, columns);
    }

}
