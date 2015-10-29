package database.utils;

public class LessOrEqualsValueComparator extends AbstractValueComparator {
	
	public LessOrEqualsValueComparator(int constantValue) {
		super(constantValue);
	}
	
	public LessOrEqualsValueComparator(String constantValue) {
		super(constantValue);
	}

	@Override
	public boolean isValid(int value) {
		return constantInt <= value;
	}

	@Override
	public boolean isValid(int valueA, int valueB) {
		return valueA <= valueB;
	}

	@Override
	public boolean isValid(String value) {
		return constantString.compareTo(value) <= 0;
	}

	@Override
	public boolean isValid(String valueA, String valueB) {
		return valueA.compareTo(valueB) <= 0;
	}

}
