package database.command;

import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;

public class DescribeTableCommandExecutor implements ICommandExecutor {

    private ITableDef tableDef;

    public DescribeTableCommandExecutor(ITableDef tableDef) {
        this.tableDef = tableDef;
    }

    @Override
    public CommandResult execute() {
        CommandResult commandResult = new CommandResult();
        commandResult.addColumn("Nome da Coluna");
        commandResult.addColumn("Tipo");
        commandResult.addColumn("Tamanho");

        for (IColumnDef column : tableDef.getColumns()) {
            commandResult.addValue("Nome da Coluna", column.getName());
            commandResult.addValue("Tipo", column.getDataType().toString());
            commandResult.addValue("Tamanho", String.valueOf(column.getCapacity()));
        }

        return commandResult;
    }

    public ITableDef getTable() {
        return tableDef;
    }

}
