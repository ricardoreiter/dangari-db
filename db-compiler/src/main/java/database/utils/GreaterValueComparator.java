package database.utils;

import database.metadata.interfaces.IColumnDef;

public class GreaterValueComparator extends AbstractValueComparator {

    public GreaterValueComparator(Object constantValue) {
        super(constantValue);
    }

    public GreaterValueComparator(Object constantValue, IColumnDef columnLeft, IColumnDef columnRight) {
        super(constantValue, columnLeft, columnRight);
    }

    @Override
    public boolean isValid(Object valueRight) {
        return ((Comparable<Object>) constantValue).compareTo(valueRight) > 0;
    }

    @Override
    public boolean isValid(Object valueA, Object valueB) {
        return ((Comparable<Object>) valueA).compareTo(valueB) > 0;
    }

}
