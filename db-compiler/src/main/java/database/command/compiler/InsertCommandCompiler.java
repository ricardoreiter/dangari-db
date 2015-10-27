/*
 * Created on 05/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.command.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

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
public class InsertCommandCompiler implements ICommandCompiler {

    private String table;
    private LinkedList<String> fields = new LinkedList<>();
    private HashMap<String, CompilerDataType> values = new LinkedHashMap<>();

    @Override
    public void accept(int action, Token token) {
        System.out.println("SELECT_COMMAND_COMPILER - Ação #" + action + ", Token: " + token);
        switch (action) {
            case 10:
                values.put(token.getLexeme(), CompilerDataType.NUMBER);
                break;
            case 11:
                values.put(token.getLexeme(), CompilerDataType.LITERAL);
                break;
            case 12:
                values.put(token.getLexeme(), CompilerDataType.NULL);
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

        Set<Entry<String, CompilerDataType>> entrySet = values.entrySet();
        int i = 0;
        for (Entry<String, CompilerDataType> entry : entrySet) {
            CompilerDataType compilerDataType = entry.getValue();
            if (!compilerDataType.getDataTypes().contains(columns.get(i).getDataType())) {
                throw new SemanticError(String.format("Tipo [%s] incompatível com o campo [%s]. Era esperado um [%s]", compilerDataType, columns.get(i).getName(), columns.get(i).getDataType()));
            }
            i++;
        }
        return null;
    }

}
