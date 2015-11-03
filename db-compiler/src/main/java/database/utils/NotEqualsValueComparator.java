package database.utils;

import database.metadata.interfaces.IColumnDef;

public class NotEqualsValueComparator extends AbstractValueComparator {

    public NotEqualsValueComparator(Object constantValue) {
        super(constantValue);
    }

    public NotEqualsValueComparator(Object constantValue, IColumnDef columnLeft) {
        super(constantValue, columnLeft);
    }

    public NotEqualsValueComparator(Object constantValue, IColumnDef columnLeft, IColumnDef columnRight) {
        super(constantValue, columnLeft, columnRight);
    }

    @Override
    public boolean isValid(Object valueRight) {
        return !constantValue.equals(valueRight);
    }

    @Override
    public boolean isValid(Object valueA, Object valueB) {
        return !valueA.equals(valueB);
    }

}
