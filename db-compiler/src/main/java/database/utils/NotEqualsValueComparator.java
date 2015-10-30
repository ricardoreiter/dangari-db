package database.utils;

public class NotEqualsValueComparator extends AbstractValueComparator {

    public NotEqualsValueComparator(Object constantValue) {
        super(constantValue);
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
