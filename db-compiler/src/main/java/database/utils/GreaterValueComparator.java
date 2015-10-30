package database.utils;

public class GreaterValueComparator extends AbstractValueComparator {

    public GreaterValueComparator(Object constantValue) {
        super(constantValue);
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
