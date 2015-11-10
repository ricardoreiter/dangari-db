package database.command;

import database.manager.DatabaseManager;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

public class DropTableCommandExecutor implements ICommandExecutor {

    private ITableDef tableDef;

    public DropTableCommandExecutor(ITableDef tableDef) {
        this.tableDef = tableDef;
    }

    @Override
    public CommandResult execute() {
        IDatabaseDef database = DatabaseManager.INSTANCE.getActualDatabase();
        database.removeTable(tableDef);
        DatabaseManager.INSTANCE.refreshDatabase();

        CommandResult commandResult = new CommandResult();
        commandResult.addColumn("Info");
        commandResult.addValue("Info", String.format("Tabela [%s] removida.", tableDef.getName()));
        return commandResult;
    }

}
