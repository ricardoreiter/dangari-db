package database.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.utils.Fn;

public class FileManager {

	private static final String ROOT_DIR = System.getProperty("java.io.tmpdir") + "DANGARI";
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

	public static Map<String, File> getDatabases() {
		return databases;
	}

	public static File getDatabase(String name) {
		return databases.get(name);
	}

	public static void deleteDatabase(String database) {
		checkDatabase(database);
		File databaseFile = databases.get(database);

		if (!delete(databaseFile)) {
			throw new RuntimeException(String.format("Não foi possível deletar a base de dados %s", database));
		}

		databases.remove(database);
	}

	private static boolean delete(File file) {
		File[] listFiles = file.listFiles();

		for (File inner : listFiles) {
			if (inner.isDirectory()) {
				if (!delete(inner)) {
					return false;
				}
			} else {
				if (!inner.delete()) {
					return false;
				}
			}
		}

		return file.delete();
	}

	public static File createDatabase(String name) {
		if (databases.containsKey(name)) {
			throw new RuntimeException(String.format("Já existe uma base de dados com o nome %s", name));
		}

		String databasePath = ROOT.getAbsolutePath();

		File databaseFile = new File(String.format("%s%s%s", databasePath, File.separator, name));

		if (!databaseFile.mkdirs()) {
			throw new RuntimeException(String.format("Erro ao criar a base de dados %s", name));
		}

		databases.put(name, databaseFile);

		return databaseFile;
	}

	public static File[] getTablesBy(String databaseName) {
		checkDatabase(databaseName);

		File databaseFile = databases.get(databaseName);

		return getTablesBy(databaseFile);
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

	public static File createTable(String databaseName, String tableName) {
		checkDatabase(databaseName);

		File database = databases.get(databaseName);

		String databasePath = database.getAbsolutePath();

		File tableDir = new File(databasePath + File.separator + tableName);

		if (!tableDir.mkdirs()) {
			throw new RuntimeException(
					String.format("Não foi possível criar o diretório %s", tableDir.getAbsolutePath()));
		}

		String tablePath = tableDir.getAbsolutePath();

		persistFile(String.format("%s%s%s%s", tablePath, File.separator, tableName, ".def"));

		persistFile(String.format("%s%s%s%s", tablePath, File.separator, tableName, ".dat"));

		return tableDir;
	}

	public static void createIndex(final File table, final String columnName) {
		final String tablePath = table.getAbsolutePath();

		persistFile(String.format("%s%s%s%s", tablePath, File.separator, columnName, ".idx"));
	}

	public static File getIndex(final File table, final String columnName) {
		File[] files = table.listFiles();

		return findFile(files, columnName);
	}

	private static File findFile(final File[] files, final String fileName) {
		for (File file : files) {
			if (file.getName().startsWith(fileName)) {
				return file;
			}
		}
		return null;
	}

	private static File persistFile(String pathFile) {
		File file = new File(pathFile);
		if (file.exists()) {
			throw new RuntimeException(String.format("Já arquivo %s já existe.", pathFile));
		}

		try {
			if (!file.createNewFile()) {
				throw new RuntimeException(String.format("Não foi possível criar o arquivo %s", pathFile));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return file;
	}

	private static void checkDatabase(String database) {
		if (!databases.containsKey(database)) {
			throw new RuntimeException(String.format("A base de dados %s não existe", database));
		}
	}
}
