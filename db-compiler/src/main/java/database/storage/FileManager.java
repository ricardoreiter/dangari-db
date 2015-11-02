package database.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.utils.Fn;

public class FileManager {

	private static final String ROOT_DIR = System.getProperty("java.io.tmpdir") + File.separator + "DANGARI";
	private static final File ROOT = new File(ROOT_DIR);
	private static final Map<String, File> databases = new HashMap<>();

	static {
		if (!ROOT.exists()) {
			ROOT.mkdirs();
		} else {
			File[] listFiles = ROOT.listFiles();
			Fn.apply(db -> databases.put(db.getName(), db), listFiles);
		}
	}

	public static File getDatabase(String name) {
		return databases.get(name);
	}

	public static File[] getTablesBy(File database) {
		File[] files = database.listFiles();

		List<File> lst = new ArrayList<>();
		for (File file : files) {
			if (file != null && file.isDirectory()) {
				lst.add(file);
			}
		}

		return lst.toArray(new File[lst.size()]);
	}
}
