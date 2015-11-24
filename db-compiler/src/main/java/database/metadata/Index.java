package database.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Index implements Serializable {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;

	private final Map<String, Set<Integer>> indexes = new HashMap<>();

	public Set<Integer> get(final Object value) {
		final String strValue = value.toString();
		if (indexes.containsKey(strValue)) {
			return indexes.get(strValue);
		}
		return Collections.emptySet();
	}

	public void put(final Object key, final Integer index) {
		final String strKey = key.toString();

		final Set<Integer> values;
		if (indexes.containsKey(strKey)) {
			values = indexes.get(strKey);
		} else {
			values = new TreeSet<>();
			indexes.put(strKey, values);
		}

		values.add(index);
	}

}
