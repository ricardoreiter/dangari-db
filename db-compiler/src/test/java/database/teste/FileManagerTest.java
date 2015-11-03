package database.teste;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import database.storage.FileManager;

public class FileManagerTest {

	private static final String DATABASE_TEST_NAME = "DATABASE_TEST";

	@After
	public void clear() {
		File database = FileManager.getDatabase(DATABASE_TEST_NAME);
		if (database != null && database.exists()) {
			database.setWritable(true);
			FileManager.deleteDatabase(DATABASE_TEST_NAME);
		}
	}

	@Test
	public void createAndGetDatabaseTest() {
		String databaseName = DATABASE_TEST_NAME;

		if (FileManager.getDatabase(databaseName) != null) {
			FileManager.deleteDatabase(databaseName);
		}

		File databaseFile = FileManager.createDatabase(databaseName);

		File databaseResult = FileManager.getDatabase(databaseName);

		Assert.assertSame(databaseFile, databaseResult);

		FileManager.deleteDatabase(databaseName);

		databaseResult = FileManager.getDatabase(databaseName);

		Assert.assertNull(databaseResult);
	}

	@Test
	public void createTableTest() {
		FileManager.createDatabase(DATABASE_TEST_NAME);

		String tableName = "TABLE_TEST";
		File table = FileManager.createTable(DATABASE_TEST_NAME, tableName);

		Assert.assertTrue(table.exists());
		Assert.assertTrue(table.isDirectory());
		Assert.assertEquals(2, table.list().length);
	}

}
