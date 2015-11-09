package database.utils;

import database.metadata.interfaces.IColumnDef;

public class EqualsValueComparator extends AbstractValueComparator {

    public EqualsValueComparator(Object constantValue, IColumnDef columnLeft) {
        super(constantValue, columnLeft);
    }

    public EqualsValueComparator(Object constantValue, IColumnDef columnLeft, IColumnDef columnRight) {
        super(constantValue, columnLeft, columnRight);
    }

    @Override
    public boolean isValid(Object valueRight) {
        return getConstantValue().equals(valueRight);
    }

    @Override
    public boolean isValid(Object valueA, Object valueB) {
        return valueA.equals(valueB);
    }

}
