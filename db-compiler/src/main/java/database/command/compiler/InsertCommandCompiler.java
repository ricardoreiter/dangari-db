/*
 * Created on 05/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.command.compiler;

import java.util.ArrayList;
import java.util.LinkedList;

import database.command.ICommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;
import database.metadata.DataType;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

/**
 * @author ricardo.reiter
 */
public class InsertCommandCompiler implements ICommandCompiler {

    private String table;
    private LinkedList<String> fields = new LinkedList<>();

    private class InsertValue {

        String value;
        CompilerDataType type;
    }

    private LinkedList<InsertValue> values = new LinkedList<>();

    @Override
    public void accept(int action, Token token) {
        System.out.println("SELECT_COMMAND_COMPILER - Ação #" + action + ", Token: " + token);
        switch (action) {
            case 10:
                InsertValue value = new InsertValue();
                value.value = token.getLexeme().replace("\"", "");
                value.type = CompilerDataType.NUMBER;
                values.add(value);
                break;
            case 11:
                value = new InsertValue();
                value.value = token.getLexeme().replace("\"", "");
                value.type = CompilerDataType.LITERAL;
                values.add(value);
                break;
            case 12:
                value = new InsertValue();
                value.value = token.getLexeme().replace("\"", "");
                value.type = CompilerDataType.NULL;
                values.add(value);
                break;
            case 30:
                table = token.getLexeme();
                break;
            case 31:
            case 32:
            case 33:
            case 34:
                break;
            case 35:
                fields.add(token.getLexeme());
        }
    }

    @Override
    public ICommandExecutor compile() throws SemanticError {
        IDatabaseDef database = DatabaseManager.INSTANCE.getDatabase();
        ITableDef tableDef = database.getTableDef(table);
        if (tableDef == null) {
            throw new SemanticError("Tabela [" + table + "] não existe");
        }

        ArrayList<IColumnDef> columns = new ArrayList<IColumnDef>(fields.size());
        for (String field : fields) {
            IColumnDef actualColumn = tableDef.getColumnDef(field);
            if (actualColumn == null) {
                throw new SemanticError("Campo [" + field + "] não existe na tabela [" + table + "]");
            }
            columns.add(actualColumn);
        }

        if (fields.size() != values.size()) {
            throw new SemanticError("Quantidade de campos informados é diferente da quantidade de valores informados");
        }

        int i = 0;
        for (InsertValue insertValue : values) {
            CompilerDataType compilerDataType = insertValue.type;
            IColumnDef columnDef = columns.get(i);
            if (!compilerDataType.getDataTypes().contains(columnDef.getDataType())) {
                throw new SemanticError(String.format("Tipo [%s] incompatível com o campo [%s]. Era esperado um [%s]", compilerDataType, columnDef.getName(), columnDef.getDataType()));
            }

            if (compilerDataType == CompilerDataType.LITERAL) {
                int columnCapacity = columnDef.getCapacity().intValue();
                int literalSize = insertValue.value.length();
                if (columnDef.getDataType() == DataType.CHAR) {
                    if (columnCapacity != literalSize) {
                        throw new SemanticError(String.format("Tamanho do CHARACTER informado diferente do tamanho do campo, era esperando um CHARACTER com [%s] caracteres, mas encontrou [%s]", columnCapacity, literalSize));
                    }
                } else {
                    if (literalSize > columnCapacity) {
                        throw new SemanticError(String.format("Tamanho do VARCHAR informado diferente do tamanho do campo, era esperando um VARCHAR com até [%s] caracteres, mas encontrou [%s]", columnCapacity, literalSize));
                    }
                }
            }
            i++;
        }
        return null;
    }

}
