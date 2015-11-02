package database.command;


public class CreateCommandExecutor implements ICommandExecutor {

    private String tableName;

    @Override
    public IBuffer[] execute() {
        //		DatabaseManager.INSTANCE.createTable(tableName);

        return null;
    }

}
