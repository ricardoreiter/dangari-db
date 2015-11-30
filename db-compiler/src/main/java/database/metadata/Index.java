package database.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Index implements Serializable {

    public class IndexEntry implements Comparable<IndexEntry>, Serializable {

        private static final long serialVersionUID = 1L;

        public Object value;
        public LinkedList<Integer> indexes = new LinkedList<Integer>();

        @SuppressWarnings("unchecked")
        @Override
        public int compareTo(IndexEntry ie) {
            return ((Comparable<Object>) value).compareTo(ie.value);
        }

    }

    /**
     * Serial Version
     */
    private static final long serialVersionUID = 1L;

    private final ArrayList<IndexEntry> values = new ArrayList<>();

    public ArrayList<IndexEntry> getValues() {
        return values;
    }

    public LinkedList<Integer> get(final Object value) {
        int index = binarySearch(value);
        if (index >= 0) {
            return values.get(index).indexes;
        }
        return new LinkedList<Integer>();
    }

    private int binarySearch(final Object value) {
        IndexEntry index = new IndexEntry();
        index.value = value;
        return Collections.binarySearch(values, index);
    }

    public LinkedList<Integer> getIndexesEquals(final Object value) {
        return get(value);
    }

    public LinkedList<Integer> getIndexesNotEquals(final Object value) {
        int index = binarySearch(value);
        LinkedList<Integer> indexes = new LinkedList<Integer>();
        for (int i = 0; i < values.size(); i++) {
            if (i == index) {
                continue;
            }
            indexes.addAll(values.get(i).indexes);
        }
        return indexes;
    }

    public LinkedList<Integer> getIndexesGreater(final Object value) {
        return internalGetIndexesGreater(value, false);
    }

    public LinkedList<Integer> getIndexesGreaterOrEquals(final Object value) {
        return internalGetIndexesGreater(value, true);
    }

    public LinkedList<Integer> getIndexesLess(final Object value) {
        return internalGetIndexesLess(value, false);
    }

    public LinkedList<Integer> getIndexesLessOrEquals(final Object value) {
        return internalGetIndexesLess(value, true);
    }

    private LinkedList<Integer> internalGetIndexesLess(final Object value, boolean inclusive) {
        LinkedList<Integer> indexes = new LinkedList<>();

        int index = binarySearch(value);
        int finalIndex;
        if (inclusive) {
            finalIndex = (index >= 0) ? index + 1 : (index * -1) - 1;
        } else {
            finalIndex = (index >= 0) ? index : (index * -1) - 1;
        }
        for (int i = 0; i < finalIndex; i++) {
            indexes.addAll(values.get(i).indexes);
        }
        return indexes;
    }

    private LinkedList<Integer> internalGetIndexesGreater(final Object value, boolean inclusive) {
        LinkedList<Integer> indexes = new LinkedList<>();

        int index = binarySearch(value);
        int initialIndex;
        if (inclusive) {
            initialIndex = (index >= 0) ? index : (index * -1) - 1;
        } else {
            initialIndex = (index >= 0) ? index + 1 : (index * -1) - 1;
        }
        for (int i = initialIndex; i < values.size(); i++) {
            indexes.addAll(values.get(i).indexes);
        }
        return indexes;
    }

    public void put(final Object key, final Integer index) {
        LinkedList<Integer> indexes = get(key);

        indexes.add(index);

        if (indexes.size() == 1) {
            IndexEntry entry = new IndexEntry();
            entry.value = key;
            entry.indexes = indexes;
            values.add(entry);
            Collections.sort(values);
        }
    }

}
