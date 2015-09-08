package database.view;

public class Column {

	private String name;
	private String alias;

	public Column(String name) {
		this.name = name;
	}

	public Column(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		if (alias == null) {
			return name;
		}
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		if (alias != null) {
			return alias;
		}
		return name;
	}

}
