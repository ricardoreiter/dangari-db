package database.metadata;

import database.metadata.interfaces.IColumnDef;

public class ColumnDef implements IColumnDef {

	private String name;
	private DataType dataType;
	private int capacity;

	@Override
	public String getName() {
		return name;
	}

	public ColumnDef(String name, DataType dataType, int capacity) {
		setName(name);
		setDataType(dataType);
		setCapacity(capacity);
	}

	@Override
	public void setName(String name) {
		checkName(name);
		this.name = name;
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public void setDataType(DataType dataType) {
		checkDataType(dataType);
		this.dataType = dataType;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void setCapacity(int capacity) {
		checkCapacity(capacity);
		this.capacity = capacity;
	}

	private void checkName(String name) {
		if (name == null || name.isEmpty()) {
			throw new RuntimeException("O nome da coluna não pode ser nulo ou vazio");
		}
	}

	private void checkDataType(DataType dataType) {
		if (dataType == null) {
			throw new RuntimeException("O tipo de dado da coluna não pode ser nulo");
		}
	}

	private void checkCapacity(int capacity) {
		if (dataType == DataType.INTEGER) {
			return;
		}

		if (capacity < 1) {
			throw new RuntimeException("A capacidade da coluna não pode ser menor que 1");
		}
	}
}
