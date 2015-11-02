package database.metadata.interfaces;

import database.metadata.DataType;

public interface IColumnDef {

	String getName();

	void setName(String name);

	DataType getDataType();

	void setDataType(DataType dataType);

	int getCapacity();

	void setCapacity(int capacity);

}
