package database.utils;

import java.util.LinkedList;

import database.metadata.Index;
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

    @Override
    public LinkedList<Integer> getIndexes(Index index, Object value) {
        return index.getIndexesEquals(value);
    }

}
