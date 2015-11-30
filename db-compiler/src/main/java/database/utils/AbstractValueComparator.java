package database.utils;

import java.util.LinkedList;

import database.metadata.Index;
import database.metadata.interfaces.IColumnDef;

public abstract class AbstractValueComparator {

    private int order;
    private final Object constantValue;
    private final IColumnDef columnLeft;
    private final IColumnDef columnRight;

    public abstract boolean isValid(Object valueRight);

    public abstract boolean isValid(Object valueA, Object valueB);

    public abstract LinkedList<Integer> getIndexes(Index index, Object value);

    public AbstractValueComparator(Object constantValue, IColumnDef columnLeft) {
        this.constantValue = constantValue;
        this.columnLeft = columnLeft;
        this.columnRight = null;
    }

    public AbstractValueComparator(Object constantValue, IColumnDef columnLeft, IColumnDef columnRight) {
        this.constantValue = constantValue;
        this.columnLeft = columnLeft;
        this.columnRight = columnRight;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @return the columnLeft
     */
    public IColumnDef getColumnLeft() {
        return columnLeft;
    }

    /**
     * @return the columnRight
     */
    public IColumnDef getColumnRight() {
        return columnRight;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the constantValue
     */
    public Object getConstantValue() {
        return constantValue;
    }

}
