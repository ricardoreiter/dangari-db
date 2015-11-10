/*
 * Created on 05/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.command.compiler;

import database.command.DescribeTableCommandExecutor;
import database.command.ICommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

/**
 * @author ricardo.reiter
 */
public class DescribeTableCommandCompiler implements ICommandCompiler {

    private String table;

    @Override
    public void accept(int action, Token token) {
        switch (action) {
            case 62:
                this.table = token.getLexeme();
                break;
        }
    }

    @Override
    public ICommandExecutor compile() throws SemanticError {
        IDatabaseDef database = DatabaseManager.INSTANCE.getActualDatabase();
        ITableDef tableDef = database.getTableDef(table);
        if (tableDef == null) {
            throw new SemanticError("Tabela [" + table + "] não existe");
        }

        return new DescribeTableCommandExecutor(tableDef);
    }

}
