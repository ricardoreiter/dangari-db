package database.utils;

import java.util.LinkedList;

import database.metadata.Index;
import database.metadata.interfaces.IColumnDef;

public class LessOrEqualsValueComparator extends AbstractValueComparator {

    public LessOrEqualsValueComparator(Object constantValue, IColumnDef columnLeft, IColumnDef columnRight) {
        super(constantValue, columnLeft, columnRight);
    }

    @Override
    public boolean isValid(Object valueLeft) {
        return ((Comparable<Object>) valueLeft).compareTo(getConstantValue()) <= 0;
    }

    @Override
    public boolean isValid(Object valueA, Object valueB) {
        return ((Comparable<Object>) valueA).compareTo(valueB) <= 0;
    }

    @Override
    public LinkedList<Integer> getIndexes(Index index, Object value) {
        return index.getIndexesLessOrEquals(value);
    }

}
