package database.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Index implements Serializable {

	private class IndexEntry implements Comparable<IndexEntry> {

		public Object value;
		public Set<Integer> indexes = new HashSet<Integer>();
		
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

	public Set<Integer> get(final Object value) {
		int index = binarySearch(value);
		if (index >= 0) {
			return values.get(index).indexes;
		}
		return new HashSet<>();
	}

	private int binarySearch(final Object value) {
		IndexEntry index = new IndexEntry();
		index.value = value;
		return Collections.binarySearch(values, index);
	}
	
	public Set<Integer> getIndexesEquals(final Object value) {
		return get(value);
	}
	
	public Set<Integer> getIndexesNotEquals(final Object value) {
		int index = binarySearch(value);
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
		
		int index = binarySearch(value);
		int finalIndex;
		if (inclusive) {
			finalIndex = (index >= 0) ? index + 1 : (index * -1);
		} else {
			finalIndex = (index >= 0) ? index : (index * -1);
		}
		for (int i = 0; i < finalIndex; i++) {
			indexes.addAll(values.get(i).indexes);
		}
		return indexes;
	}
	
	private Set<Integer> internalGetIndexesGreater(final Object value, boolean inclusive) {
		Set<Integer> indexes = new TreeSet<Integer>();
		
		int index = binarySearch(value);
		if (index == -values.size()) {
			return Collections.emptySet();
		}
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
