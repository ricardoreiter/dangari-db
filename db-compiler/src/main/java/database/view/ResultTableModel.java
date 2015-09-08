package database.view;

import javax.swing.table.DefaultTableModel;

public class ResultTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private final Result result;

	public ResultTableModel(Result result) {
		this.result = result;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		if (result != null && result.getCols() != null) {
			return result.getCols().length;
		}
		return 0;
	}

	@Override
	public String getColumnName(int column) {
		if (result != null && result.getCols() != null) {
			return result.getCols()[column].getAlias();
		}
		return "Nome não definido";
	}

	public Column getColumn(int column) {
		if (result != null && result.getCols() != null) {
			return result.getCols()[column];
		}
		return null;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (result != null && result.getResultMap() != null) {
			return result.getResultMap().get(getColumn(column)).get(row);
		}
		return super.getValueAt(row, column);
	}

	@Override
	public int getRowCount() {
		if (result != null && result.getResultMap() != null) {
			return result.getResultMap().get(result.getCols()[0]).size();
		}
		return super.getRowCount();
	}

}
