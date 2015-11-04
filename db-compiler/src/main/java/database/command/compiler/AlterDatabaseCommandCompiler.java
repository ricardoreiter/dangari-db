package database.command.compiler;

import database.command.AlterDatabaseCommandExecutor;
import database.command.ICommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;

public class AlterDatabaseCommandCompiler implements ICommandCompiler {

	private String databaseName;
	
	@Override
	public void accept(int action, Token token) {
		switch (action) {
		case 63:
			databaseName = token.getLexeme();
			break;

		default:
			break;
		}
	}

	@Override
	public ICommandExecutor compile() throws SemanticError {
		if (DatabaseManager.INSTANCE.getDatabase(databaseName) == null) {
			throw new SemanticError(String.format("Database [%s] não existe.", databaseName));
		}
		return new AlterDatabaseCommandExecutor(databaseName);
	}

}
