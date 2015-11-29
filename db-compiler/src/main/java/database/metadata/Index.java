package database.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Index implements Serializable {

	private class IndexEntry implements Comparable<Object> {

		public Object value;
		public Set<Integer> indexes = new TreeSet<Integer>();
		
		@Override
		public int compareTo(Object o) {
			return ((Comparable<Object>) value).compareTo(o);
		}
		
	}
	
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	private final ArrayList<IndexEntry> values = new ArrayList<>();

	public Set<Integer> get(final Object value) {
		int index = Collections.binarySearch(values, value);
		if (index >= 0) {
			return values.get(index).indexes;
		}
		return new TreeSet<>();
	}
	
	public Set<Integer> getIndexesEquals(final Object value) {
		return get(value);
	}
	
	public Set<Integer> getIndexesNotEquals(final Object value) {
		int index = Collections.binarySearch(values, value);
		Set<Integer> indexes = new TreeSet<Integer>();
		for (int i = 0; i < values.size(); i++) {
			if (i == index) {
				continue;
			}
			indexes.addAll(values.get(i).indexes);
		}
		return indexes;
	}
	
	public Set<Integer> getIndexesGreater(final Object value) {
		return internalGetIndexesGreater(value, false);
	}
	
	public Set<Integer> getIndexesGreaterOrEquals(final Object value) {
		return internalGetIndexesGreater(value, true);
	}
	
	public Set<Integer> getIndexesLess(final Object value) {
		return internalGetIndexesLess(value, false);
	}
	
	public Set<Integer> getIndexesLessOrEquals(final Object value) {
		return internalGetIndexesLess(value, true);
	}

	private Set<Integer> internalGetIndexesLess(final Object value, boolean inclusive) {
		Set<Integer> indexes = new TreeSet<Integer>();
		
		int index = Collections.binarySearch(values, value);
		int finalIndex = (index >= 0) ? ((inclusive) ? index + 1 : index) : values.size();
		for (int i = 0; i < finalIndex; i++) {
			indexes.addAll(values.get(i).indexes);
		}
		return indexes;
	}
	
	private Set<Integer> internalGetIndexesGreater(final Object value, boolean inclusive) {
		Set<Integer> indexes = new TreeSet<Integer>();
		
		int index = Collections.binarySearch(values, value);
		int initialIndex = (index >= 0) ? ((inclusive) ? index : index + 1) : 0;
		for (int i = initialIndex; i < values.size(); i++) {
			indexes.addAll(values.get(i).indexes);
		}
		return indexes;
	}

	public void put(final Object key, final Integer index) {
		Set<Integer> indexes = get(key);
		
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
