package database.command;

import database.manager.DatabaseManager;

public class AlterDatabaseCommandExecutor implements ICommandExecutor {

    private String databaseName;

    public AlterDatabaseCommandExecutor(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public CommandResult execute() {
        DatabaseManager.INSTANCE.setDatabase(databaseName);
        CommandResult commandResult = new CommandResult();
        commandResult.addColumn("Info");
        commandResult.addValue("Info", String.format("O [%s] é o database ativo agora.", databaseName));
        return commandResult;
    }

}
