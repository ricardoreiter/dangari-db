package database.utils;

public class OrBooleanComparator extends AbstractBooleanComparator {

	@Override
	public boolean isValid(boolean booleanA, boolean booleanB) {
		return booleanA || booleanB;
	}

}
