package database.command;

import database.manager.DatabaseManager;

public class AlterDatabaseCommandExecutor implements ICommandExecutor {

	private String databaseName;
	
	public AlterDatabaseCommandExecutor(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public IBuffer[] execute() {
		DatabaseManager.INSTANCE.setDatabase(databaseName);
		return null;
	}

}
