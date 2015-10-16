package database.command.compiler;

import database.command.ICommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;

public interface ICommandCompiler {

    void accept(int action, Token token);

    ICommandExecutor compile() throws SemanticError;

}
