package database.utils;

import database.metadata.interfaces.IColumnDef;

public abstract class AbstractValueComparator {

    protected int order;
    protected final Object constantValue;
    protected IColumnDef columnLeft;
    protected IColumnDef columnRight;

    public abstract boolean isValid(Object valueRight);

    public abstract boolean isValid(Object valueA, Object valueB);

    public AbstractValueComparator(Object constantValue) {
        this.constantValue = constantValue;
    }

    public AbstractValueComparator(Object constantValue, IColumnDef columnLeft) {
        this.constantValue = constantValue;
        this.columnLeft = columnLeft;
    }

    public AbstractValueComparator(Object constantValue, IColumnDef columnLeft, IColumnDef columnRight) {
        this.constantValue = constantValue;
        this.columnLeft = columnLeft;
        this.columnRight = columnRight;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
