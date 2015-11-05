package database.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import database.metadata.DatabaseDef;
import database.metadata.interfaces.IDatabaseDef;
import database.storage.DefStorage;
import database.storage.FileManager;

public final class DatabaseManager {

    public interface DatabaseManagerListener {

        void onRefreshDatabases();
    }

    public final static DatabaseManager INSTANCE = new DatabaseManager();

    private Map<String, IDatabaseDef> databases = new HashMap<>();
    private IDatabaseDef database;
    private DatabaseManagerListener listener;

    private DatabaseManager() {
        Map<String, File> databaseFiles = FileManager.getDatabases();
        for (String key : databaseFiles.keySet()) {
            DatabaseDef databaseDef = new DatabaseDef(key);
            File[] tablesFiles = FileManager.getTablesBy(key);
            for (int i = 0; i < tablesFiles.length; i++) {
                databaseDef.addTable(DefStorage.getTableDef(tablesFiles[i]), tablesFiles[i]);
            }
            addDatabase(key, databaseDef);
        }
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public void setDatabase(String databaseName) {
        this.database = getDatabase(databaseName);
        if (listener != null) listener.onRefreshDatabases();
    }

    public void addDatabase(String name, IDatabaseDef database) {
        databases.put(name, database);
        if (listener != null) listener.onRefreshDatabases();
    }

    public IDatabaseDef getDatabase(String name) {
        return databases.get(name);
    }

    public Map<String, IDatabaseDef> getDatabases() {
        return databases;
    }

    public IDatabaseDef getActualDatabase() {
        return this.database;
    }

    public void setListener(DatabaseManagerListener listener) {
        this.listener = listener;
    }

    public void refreshDatabase() {
        if (listener != null) listener.onRefreshDatabases();
    }

}
