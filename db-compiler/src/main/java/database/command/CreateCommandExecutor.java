package database.command;

import database.manager.DatabaseManager;

public class CreateCommandExecutor implements ICommandExecutor {

	private String tableName;
	
	
	
	@Override
	public IBuffer[] execute() {
		DatabaseManager.INSTANCE.createTable(tableName);
		
		
		return null;
	}

}
