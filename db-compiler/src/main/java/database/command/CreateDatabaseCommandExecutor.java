package database.command;

import database.manager.DatabaseManager;
import database.metadata.DatabaseDef;
import database.storage.FileManager;

public class CreateDatabaseCommandExecutor implements ICommandExecutor {

    private String databaseName;

    public CreateDatabaseCommandExecutor(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public CommandResult execute() {
        FileManager.createDatabase(databaseName);
        DatabaseManager.INSTANCE.addDatabase(databaseName, new DatabaseDef(databaseName));
        CommandResult commandResult = new CommandResult();
        commandResult.addColumn("Info");
        commandResult.addValue("Info", String.format("O database [%s] foi criado com sucesso.", databaseName));
        return commandResult;
    }

}
