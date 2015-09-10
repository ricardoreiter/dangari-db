package database.metadata;

public enum DataType {

	CHAR(Character.class),

	VARCHAR(String.class),

	NUMBER(Number.class);

	private Class<?> clazz;

	private DataType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}
}
