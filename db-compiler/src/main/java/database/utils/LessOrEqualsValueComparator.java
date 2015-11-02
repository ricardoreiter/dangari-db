package database.utils;

public class LessOrEqualsValueComparator extends AbstractValueComparator {

    public LessOrEqualsValueComparator(Object constantValue) {
        super(constantValue);
    }

    @Override
    public boolean isValid(Object valueRight) {
        return ((Comparable<Object>) constantValue).compareTo(valueRight) <= 0;
    }

    @Override
    public boolean isValid(Object valueA, Object valueB) {
        return ((Comparable<Object>) valueA).compareTo(valueB) <= 0;
    }

}