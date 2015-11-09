package database.utils;

import database.metadata.interfaces.IColumnDef;

public class GreaterOrEqualsValueComparator extends AbstractValueComparator {

    public GreaterOrEqualsValueComparator(Object constantValue, IColumnDef columnLeft, IColumnDef columnRight) {
        super(constantValue, columnLeft, columnRight);
    }

    @Override
    public boolean isValid(Object valueLeft) {
        return ((Comparable<Object>) valueLeft).compareTo(getConstantValue()) >= 0;
    }

    @Override
    public boolean isValid(Object valueA, Object valueB) {
        return ((Comparable<Object>) valueA).compareTo(valueB) >= 0;
    }

}
