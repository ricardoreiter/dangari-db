/*
 * Created on 03/11/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.gals.SemanticError;
import database.metadata.Index;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.utils.AbstractBooleanComparator;
import database.utils.AbstractValueComparator;
import database.utils.AndBooleanComparator;
import database.utils.GreaterValueComparator;
import database.utils.JoinUtils;
import database.utils.JoinUtils.IRegistry;
import database.utils.JoinUtils.TableJoinRegistry;
import database.utils.NotEqualsValueComparator;

/**
 * @author ricardo.reiter
 */
public class JoinUtilsIndexTest extends JoinUtilsTest {

    @Before
    public void beforeTest() {
    }

    @After
    public void afterTest() {
    }

    @Test
    @Override
    public void testJoin001() throws SemanticError {
        IDatabaseDef database = new DatabaseMock();
        ITableDef empresaTableDef = database.getTableDef("empresa");
        ITableDef usuarioTableDef = database.getTableDef("usuario");

        TableJoinRegistry tableUsuario = new TableJoinRegistry();
        tableUsuario.tableDef = usuarioTableDef;
        createRegistry(tableUsuario, 1, "Ricardo");
        createRegistry(tableUsuario, 2, "Daniel");
        createRegistry(tableUsuario, 3, "Gabriel");
        createRegistry(tableUsuario, 4, "Dangari");

        NotEqualsValueComparator notEqualsTableComparators = new NotEqualsValueComparator("Dangari", usuarioTableDef.getColumnDef("nome"));
        notEqualsTableComparators.setOrder(0);
        tableUsuario.tableComparators.add(notEqualsTableComparators);

        GreaterValueComparator equalsValueComparator = new GreaterValueComparator(null, empresaTableDef.getColumnDef("cod"), usuarioTableDef.getColumnDef("cod"));
        equalsValueComparator.setOrder(1);
        LinkedList<AbstractValueComparator> joinConditions = new LinkedList<AbstractValueComparator>();
        joinConditions.add(equalsValueComparator);
        tableUsuario.joinConditions.put(empresaTableDef, joinConditions);

        TableJoinRegistry tableEmpresa = new TableJoinRegistry();
        tableEmpresa.tableDef = empresaTableDef;
        createRegistry(tableEmpresa, 1, "Senior");
        createRegistry(tableEmpresa, 2, "TSystems");
        createRegistry(tableEmpresa, 3, "Senior Matriz");
        createRegistry(tableEmpresa, 4, "Dangari Corporations");

        joinConditions = new LinkedList<AbstractValueComparator>();
        joinConditions.add(equalsValueComparator);
        tableEmpresa.joinConditions.put(usuarioTableDef, joinConditions);

        TableJoinRegistry[] tables = new TableJoinRegistry[] { tableUsuario, tableEmpresa };

        ArrayList<AbstractBooleanComparator> whereRelationalsOperators = new ArrayList<AbstractBooleanComparator>();
        whereRelationalsOperators.add(new AndBooleanComparator());

        List<IRegistry> registrysJoined = JoinUtils.joinTables(whereRelationalsOperators, tables);

        Assert.assertEquals(6, registrysJoined.size());

        Assert.assertEquals(2, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(3, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior Matriz", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(4, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Dangari Corporations", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(3, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Gabriel", registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(4, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Dangari Corporations", registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(4).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(4).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(4).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(3, registrysJoined.get(4).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior Matriz", registrysJoined.get(4).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(4).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(5).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(5).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(5).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(4, registrysJoined.get(5).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Dangari Corporations", registrysJoined.get(5).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(5).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    /**
     * @param tableUsuario
     */
    @Override
    protected void createRegistry(TableJoinRegistry tableUsuario, int cod, String nome) {
        IRegistry registro = new IRegistry();

        IColumnDef codColumn = tableUsuario.tableDef.getColumnDef("cod");
        IColumnDef nomColumn = tableUsuario.tableDef.getColumnDef("nome");
        registro.columnValue.put(codColumn, new Integer(cod));
        registro.columnValue.put(nomColumn, nome);

        Index index = tableUsuario.tableDef.getIndex(codColumn);
        if (index == null) {
            index = new Index(10);
            tableUsuario.tableDef.addIndex(codColumn, index);
        }
        index.put(cod, tableUsuario.tableDef.getRowsCount());

        index = tableUsuario.tableDef.getIndex(nomColumn);
        if (index == null) {
            index = new Index(10);
            tableUsuario.tableDef.addIndex(nomColumn, index);
        }
        index.put(nome, tableUsuario.tableDef.getRowsCount());

        tableUsuario.registrys.add(registro);
        tableUsuario.tableDef.incrementRowsCount();
    }
}
