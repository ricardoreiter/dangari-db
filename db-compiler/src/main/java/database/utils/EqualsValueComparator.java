package database.utils;

public class EqualsValueComparator extends AbstractValueComparator {

	public EqualsValueComparator(int constantValue) {
		super(constantValue);
	}
	
	public EqualsValueComparator(String constantValue) {
		super(constantValue);
	}

	@Override
	public boolean isValid(int value) {
		return constantInt == value;
	}

	@Override
	public boolean isValid(int valueA, int valueB) {
		return valueA == valueB;
	}

	@Override
	public boolean isValid(String value) {
		return constantString.equals(value);
	}

	@Override
	public boolean isValid(String valueA, String valueB) {
		return valueA.equals(valueB);
	}

}
