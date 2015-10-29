package database.utils;

public abstract class AbstractValueComparator {

	protected final int constantInt;
	protected final String constantString;
	
	public abstract boolean isValid(int value);
	public abstract boolean isValid(int valueA, int valueB);
	public abstract boolean isValid(String value);
	public abstract boolean isValid(String valueA, String valueB);
	
	public AbstractValueComparator(int constantValue) {
		this.constantInt = constantValue;
		this.constantString = "";
	}
	
	public AbstractValueComparator(String constantValue) {
		this.constantString = constantValue;
		this.constantInt = 0;
	}
	
	
}
