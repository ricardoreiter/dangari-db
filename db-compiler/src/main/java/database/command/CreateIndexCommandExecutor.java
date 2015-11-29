package database.command;

import java.util.List;

import database.manager.DatabaseManager;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

public class CreateIndexCommandExecutor implements ICommandExecutor {

    private ITableDef table;
    private List<IColumnDef> columns;

    public CreateIndexCommandExecutor(ITableDef table, List<IColumnDef> checkedColumns) {
        this.table = table;
        this.columns = checkedColumns;
    }

    @Override
    public CommandResult execute() {
        IDatabaseDef database = DatabaseManager.INSTANCE.getActualDatabase();
        for (IColumnDef columnDef : columns) {
            table.addIndex(columnDef, database.createIndex(table, columnDef));
        }
        DatabaseManager.INSTANCE.refreshDatabase();
        CommandResult commandResult = new CommandResult();
        commandResult.addColumn("Info");
        commandResult.addValue("Info", String.format("[%s] índices criados na tabela [%s].", columns.size(), table.getName()));
        return commandResult;
    }
}
