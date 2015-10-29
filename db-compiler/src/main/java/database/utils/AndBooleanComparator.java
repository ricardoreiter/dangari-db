package database.utils;

public class AndBooleanComparator extends AbstractBooleanComparator {

	@Override
	public boolean isValid(boolean booleanA, boolean booleanB) {
		return booleanA && booleanB;
	}

}
