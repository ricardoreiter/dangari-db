package database.teste;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.metadata.ColumnDef;
import database.metadata.DataType;
import database.metadata.TableDef;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;
import database.storage.DatabaseStorage;
import database.storage.DefStorage;
import database.storage.Result;
import junit.framework.Assert;

public class DatabaseStorageTest {

	private File tableDat;
	private File tableDef;

	@Before
	public void setUp() throws IOException {

		tableDat = new File(System.getProperty("java.io.tmpdir") + "TEST" + File.separator + "TESTE.dat");
		tableDef = new File(System.getProperty("java.io.tmpdir") + "TEST" + File.separator + "TESTE.def");

		tableDat.createNewFile();
		tableDef.createNewFile();
	}

	@After
	public void clear() {
		if (tableDat.exists()) {
			tableDat.delete();
		}

		if (tableDef.exists()) {
			tableDef.delete();
		}
	}

	@Test
	public void insertAndSelectTest() {
		IColumnDef name = new ColumnDef("USERNAME", DataType.VARCHAR, 20);
		IColumnDef id = new ColumnDef("USERID", DataType.CHAR, 4);
		IColumnDef password = new ColumnDef("PASSWORD", DataType.INTEGER, 0);

		ITableDef tableDef = new TableDef("USER", name, id, password);
		DefStorage.setTableDef(this.tableDef.getParentFile(), tableDef);

		Map<IColumnDef, String> values = new HashMap<>();
		values.put(name, "Gabriel");
		values.put(id, "gbiz");
		values.put(password, "123456789");

		DatabaseStorage.insertRecord(this.tableDat.getParentFile(), tableDef, values);
		DatabaseStorage.insertRecord(this.tableDat.getParentFile(), tableDef, values);

		Result result = DatabaseStorage.getRecords(this.tableDat.getParentFile(), tableDef);

		result.next();

		Assert.assertEquals(values.get(name), result.getAsString(name));
		Assert.assertEquals(values.get(id), result.getAsString(id));
		Assert.assertEquals(Integer.valueOf(values.get(password)), result.getAsInteger(password), 0);

		ITableDef tableDefResult = DefStorage.getTableDef(this.tableDef.getParentFile());

		Assert.assertEquals(2, tableDefResult.getRowsCount());
	}
}
