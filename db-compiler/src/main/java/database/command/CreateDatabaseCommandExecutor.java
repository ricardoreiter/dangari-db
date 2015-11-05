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
    public IBuffer[] execute() {
        FileManager.createDatabase(databaseName);
        DatabaseManager.INSTANCE.addDatabase(databaseName, new DatabaseDef(databaseName));

        return null;
    }

}
