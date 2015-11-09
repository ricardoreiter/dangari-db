package database.storage;

import java.util.Map.Entry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import database.utils.Pair;

public class FileOutputStreamLRUCache<F extends File, P extends Pair<Integer, FileOutputStream>> {

	private final LinkedHashMap<F, P> cache;

	public FileOutputStreamLRUCache(final int cacheSize) {
		final int initialCapacity = cacheSize + 1;
		final float loadFactor = 1.0f;
		final boolean accessOrder = true; // Must maintain access order to
											// consider reads when defining the
											// eldest element.

		this.cache = new LinkedHashMap<F, P>(initialCapacity, loadFactor, accessOrder) {

			private static final long serialVersionUID = -4713786404420239458L;

			/**
			 * Determines whether or not the eldest element should be removed
			 * from the map.
			 */
			protected boolean removeEldestEntry(Entry<F, P> eldest) {
				boolean b = size() > cacheSize;

				if (b) {
					P pair = eldest.getValue();
					try {
						pair.getSnd().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				return b;
			}
		};

	}

	public P put(F key, P value) {
		return cache.put(key, value);
	}

	public boolean contains(F key) {
		return cache.containsKey(key);
	}

	public P get(F key) {
		return cache.get(key);
	}

	public int size() {
		return cache.size();
	}
}
