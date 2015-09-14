package database.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import database.metadata.interfaces.IColumnDeff;
import database.metadata.interfaces.ITableDeff;
import database.utils.Fn;

public class TableDeff implements ITableDeff {

	private String name;
	private int rowsCount;
	private Map<String, IColumnDeff> columns = new LinkedHashMap<>();

	public TableDeff(String name) {
		setName(name);
	}

	public TableDeff(String name, IColumnDeff... columnsDeff) {
		this(name);
		columns = Fn.map(c -> c.getName(), columnsDeff);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		checkName(name);
		this.name = name;
	}

	@Override
	public int getColumnsCount() {
		return columns.size();
	}

	@Override
	public int getRowsCount() {
		return rowsCount;
	}

	@Override
	public void incrementRowsCount() {
		rowsCount++;
	}

	@Override
	public void decrementRowsCount() {
		rowsCount--;
	}

	@Override
	public IColumnDeff getColumnDeff(String column) {
		return columns.get(column);
	}

	@Override
	public void addColumnDeff(IColumnDeff column) {
		checkColumn(column);
		String key = column.getName();
		columns.put(key, column);
	}

	@Override
	public List<IColumnDeff> getColumns() {
		return new ArrayList<>(columns.values());
	}

	private void checkName(String name) {
		if (name == null || name.isEmpty()) {
			throw new RuntimeException("O nome da tabela não pode ser nulo ou vazio");
		}
	}

	private void checkColumn(IColumnDeff column) {
		if (column == null) {
			throw new RuntimeException("Não é possível adicionar uma coluna nula");
		}

		String key = column.getName();
		if (columns.containsKey(key)) {
			throw new RuntimeException(String.format("Já existe uma coluna com o nome %s", column));
		}
	}

	@Override
	public void deleteColumnDeff(String column) {
		columns.remove(column);

	}

}
