package database.view;

import javax.swing.table.DefaultTableModel;

import database.command.CommandResult;

public class ResultTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;
    private final CommandResult result;
    private String[] columns;

    public ResultTableModel(CommandResult commandResult) {
        this.result = commandResult;
        this.columns = new String[commandResult.getValues().size()];
        int i = 0;
        for (String column : commandResult.getValues().keySet()) {
            columns[i] = column;
            i++;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        if (result != null && result.getValues() != null) {
            return columns.length;
        }
        return 0;
    }

    @Override
    public String getColumnName(int column) {
        if (result != null && result.getValues() != null) {
            return columns[column];
        }
        return "Nome não definido";
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (result != null && result.getValues() != null) {
            return result.getValues().get(columns[column]).get(row);
        }
        return super.getValueAt(row, column);
    }

    @Override
    public int getRowCount() {
        if (result != null && result.getValues() != null) {
            return result.getValues().get(columns[0]).size();
        }
        return super.getRowCount();
    }

}
