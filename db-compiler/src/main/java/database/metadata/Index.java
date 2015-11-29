package database.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Index implements Serializable {

	private class IndexEntry implements Comparable<Object> {

		public Object value;
		public Set<Integer> indexes;
		
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
		return Collections.emptySet();
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
