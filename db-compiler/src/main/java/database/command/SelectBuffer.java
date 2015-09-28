package database.command;


/**
 * Um Buffer que é retornado de um select, contendo os valores retornados
 * 
 * @author Ricardo Reiter
 *
 */
public class SelectBuffer implements IBuffer {

	private Object[] listValues;
	
	public SelectBuffer(int fieldCount) {
		listValues = new Object[fieldCount];
	}
	
	public void addValue(int field, Object value) {
		listValues[field] = value;
	}

	@Override
	public Number getNumber(int field) {
		return (Number) listValues[field];
	}

	@Override
	public Character getCharacter(int field) {
		return (Character) listValues[field];
	}

	@Override
	public String getString(int field) {
		return (String) listValues[field];
	}
	

}
