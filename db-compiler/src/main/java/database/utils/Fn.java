package database.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class Fn {

	public static <K, V> Map<K, V> map(Function<V, K> keyExtracotr, V[] array) {
		if (array == null) {
			return null;
		}

		Map<K, V> map = new LinkedHashMap<>();
		for (V v : array) {
			if (v != null) {
				K k = keyExtracotr.apply(v);
				map.put(k, v);
			}
		}

		return map;
	}

	public static <V> void apply(Function<V, ?> predicate, V[] array) {
		if (array == null) {
			return;
		}

		for (V v : array) {
			if (v != null) {
				predicate.apply(v);
			}
		}
	}

	public static <V, R> R maybe(Function<V, R> function, V any) {
		return any != null ? function.apply(any) : null;
	}

}
