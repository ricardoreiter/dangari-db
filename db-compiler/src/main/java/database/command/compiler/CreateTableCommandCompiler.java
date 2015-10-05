/*
 * Created on 05/10/2015.
 *
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.command.compiler;

import database.command.ICommandExecutor;
import database.gals.Token;


/**
 * @author ricardo.reiter
 */
public class CreateTableCommandCompiler implements ICommandCompiler {

    /* (non-Javadoc)
     * @see database.command.compiler.ICommandCompiler#accept(int, database.gals.Token)
     */
    @Override
    public void accept(int action, Token token) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see database.command.compiler.ICommandCompiler#compile()
     */
    @Override
    public ICommandExecutor compile() {
        // TODO Auto-generated method stub
        return null;
    }

}
