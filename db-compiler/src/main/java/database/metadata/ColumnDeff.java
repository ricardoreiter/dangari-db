package database.metadata;

import database.metadata.interfaces.IColumnDeff;

public class ColumnDeff implements IColumnDeff {

	private String name;
	private DataType dataType;
	private Number capacity;

	@Override
	public String getName() {
		return name;
	}

	public ColumnDeff(String name, DataType dataType, Number capacity) {
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
	public Number getCapacity() {
		return capacity;
	}

	@Override
	public void setCapacity(Number capacity) {
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

	private void checkCapacity(Number capacity) {
		if (capacity.intValue() < 1) {
			throw new RuntimeException("A capacidade da coluna não pode ser menor que 1");
		}
	}
}
