package database.command;

import java.util.Map;

import database.manager.DatabaseManager;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;

public class InsertCommandExecutor implements ICommandExecutor {

    private ITableDef tableDef;
    private Map<IColumnDef, String> values;

    public InsertCommandExecutor(ITableDef tableDef, Map<IColumnDef, String> values) {
        this.tableDef = tableDef;
        this.values = values;
    }

    @Override
    public CommandResult execute() {
        DatabaseManager.INSTANCE.getActualDatabase().insert(tableDef, values);
        CommandResult commandResult = new CommandResult();
        commandResult.addColumn("Info");
        commandResult.addValue("Info", "Registro inserido");
        return commandResult;
    }

}
