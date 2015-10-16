package database.manager;

import database.metadata.interfaces.IDatabaseDef;

public final class DatabaseManager {

    public final static DatabaseManager INSTANCE = new DatabaseManager();

    private IDatabaseDef database;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public void setDatabase(IDatabaseDef database) {
        this.database = database;
    }

    public IDatabaseDef getDatabase() {
        return this.database;
    }

}
