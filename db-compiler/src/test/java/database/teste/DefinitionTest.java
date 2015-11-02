package database.teste;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import database.metadata.ColumnDef;
import database.metadata.DataType;
import database.metadata.TableDef;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;
import database.storage.DefStorage;

public class DefinitionTest {

	private File table;
	private DefStorage defStorage;

	@Before
	public void setUp() throws IOException {

		table = new File(System.getProperty("java.io.tmpdir") + "TEST" + File.separator + "TESTE.def");
		if (table.exists()) {
			table.delete();
		}
		table.createNewFile();

		defStorage = new DefStorage();
	}

	@Test
	public void setAndGetTableDef() {
		ITableDef tableDef = new TableDef("TABLE_TEST");

		tableDef.incrementRowsCount();
		tableDef.incrementRowsCount();
		tableDef.incrementRowsCount();
		tableDef.incrementRowsCount();

		IColumnDef columnDefA = new ColumnDef("COLUMN_A", DataType.CHAR, 5);
		tableDef.addColumnDef(columnDefA);

		IColumnDef columnDefB = new ColumnDef("COLUMN_B", DataType.VARCHAR, 50);
		tableDef.addColumnDef(columnDefB);

		IColumnDef columnDefC = new ColumnDef("COLUMN_C", DataType.INTEGER, 0);
		tableDef.addColumnDef(columnDefC);

		defStorage.setTableDef(table.getParentFile(), tableDef);

		ITableDef tableDefResult = defStorage.getTableDef(table.getParentFile());

		Assert.assertEquals(tableDefResult.getName(), tableDef.getName());
		Assert.assertEquals(tableDefResult.getColumnsCount(), tableDef.getColumnsCount());
		Assert.assertEquals(tableDefResult.getRowsCount(), tableDef.getRowsCount());
		
		IColumnDef columnDefAResult = tableDefResult.getColumns().get(0);
		Assert.assertEquals(columnDefAResult.getName(), columnDefA.getName());
		Assert.assertEquals(columnDefAResult.getCapacity(), columnDefA.getCapacity());
		Assert.assertEquals(columnDefAResult.getDataType(), columnDefA.getDataType());
		
		IColumnDef columnDefBResult = tableDefResult.getColumns().get(1);
		Assert.assertEquals(columnDefBResult.getName(), columnDefB.getName());
		Assert.assertEquals(columnDefBResult.getCapacity(), columnDefB.getCapacity());
		Assert.assertEquals(columnDefBResult.getDataType(), columnDefB.getDataType());
		
		IColumnDef columnDefCResult = tableDefResult.getColumns().get(2);
		Assert.assertEquals(columnDefCResult.getName(), columnDefC.getName());
		Assert.assertEquals(columnDefCResult.getCapacity(), columnDefC.getCapacity());
		Assert.assertEquals(columnDefCResult.getDataType(), columnDefC.getDataType());
	}

}
