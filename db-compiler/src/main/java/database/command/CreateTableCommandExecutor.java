package database.command;

import java.io.File;
import java.util.ArrayList;

import database.command.compiler.CreateTableCommandCompiler.CompilerColumn;
import database.manager.DatabaseManager;
import database.metadata.ColumnDef;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.storage.DefStorage;
import database.storage.FileManager;

public class CreateTableCommandExecutor implements ICommandExecutor {

    private String tableName;
    private ArrayList<CompilerColumn> columns = new ArrayList<>();

    public CreateTableCommandExecutor(String tableName, ArrayList<CompilerColumn> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public IBuffer[] execute() {
        IDatabaseDef database = DatabaseManager.INSTANCE.getActualDatabase();
        File tableFile = FileManager.createTable(database.getName(), tableName);

        IColumnDef[] columnsDef = new IColumnDef[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            CompilerColumn compilerColumn = columns.get(i);
            columnsDef[i] = new ColumnDef(compilerColumn.columnName, compilerColumn.type, (int) compilerColumn.capacity);
        }

        ITableDef tableDef = database.createTable(tableName, columnsDef);
        DefStorage.setTableDef(tableFile, tableDef);
        database.addTable(tableDef, tableFile);
        DatabaseManager.INSTANCE.refreshDatabase();

        return null;
    }
}
