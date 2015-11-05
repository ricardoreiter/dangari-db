package database.command.compiler;

import database.command.CreateDatabaseCommandExecutor;
import database.command.ICommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;

public class CreateDatabaseCommandCompiler implements ICommandCompiler {

    private String databaseName;

    @Override
    public void accept(int action, Token token) {
        switch (action) {
            case 20:
                this.databaseName = token.getLexeme();
                break;

            default:
                break;
        }
    }

    @Override
    public ICommandExecutor compile() throws SemanticError {
        if (DatabaseManager.INSTANCE.getDatabase(databaseName) != null) {
            throw new SemanticError(String.format("Database [%s] já existente", databaseName));
        }
        return new CreateDatabaseCommandExecutor(databaseName);
    }

}
