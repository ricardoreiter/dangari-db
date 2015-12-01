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

import org.junit.Test;

import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.utils.AbstractBooleanComparator;
import database.utils.AbstractValueComparator;
import database.utils.AndBooleanComparator;
import database.utils.EqualsValueComparator;
import database.utils.GreaterOrEqualsValueComparator;
import database.utils.GreaterValueComparator;
import database.utils.JoinUtils;
import database.utils.JoinUtils.IRegistry;
import database.utils.JoinUtils.TableJoinRegistry;
import database.utils.LessOrEqualsValueComparator;
import database.utils.LessValueComparator;
import database.utils.NotEqualsValueComparator;

/**
 * @author ricardo.reiter
 */
public class JoinUtilsTest {

    @Test
    public void testJoin001() {
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

        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(3, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior Matriz", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(4, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Dangari Corporations", registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(3, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior Matriz", registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(4).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(4).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(4).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(4, registrysJoined.get(4).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Dangari Corporations", registrysJoined.get(4).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(4).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(3, registrysJoined.get(5).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Gabriel", registrysJoined.get(5).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(5).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(4, registrysJoined.get(5).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Dangari Corporations", registrysJoined.get(5).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(5).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    @Test
    public void testJoin002() {
        IDatabaseDef database = new DatabaseMock();
        ITableDef empresaTableDef = database.getTableDef("empresa");
        ITableDef usuarioTableDef = database.getTableDef("usuario");

        TableJoinRegistry tableUsuario = new TableJoinRegistry();
        tableUsuario.tableDef = usuarioTableDef;
        createRegistry(tableUsuario, 1, "Ricardo");
        createRegistry(tableUsuario, 2, "Daniel");

        TableJoinRegistry tableEmpresa = new TableJoinRegistry();
        tableEmpresa.tableDef = empresaTableDef;
        createRegistry(tableEmpresa, 1, "Senior");
        createRegistry(tableEmpresa, 2, "TSystems");

        TableJoinRegistry[] tables = new TableJoinRegistry[] { tableUsuario, tableEmpresa };

        ArrayList<AbstractBooleanComparator> whereRelationalsOperators = new ArrayList<AbstractBooleanComparator>();

        List<IRegistry> registrysJoined = JoinUtils.joinTables(whereRelationalsOperators, tables);

        Assert.assertEquals(4, registrysJoined.size());

        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    @Test
    public void testJoin003() {
        IDatabaseDef database = new DatabaseMock();
        ITableDef empresaTableDef = database.getTableDef("empresa");
        ITableDef usuarioTableDef = database.getTableDef("usuario");

        TableJoinRegistry tableUsuario = new TableJoinRegistry();
        tableUsuario.tableDef = usuarioTableDef;
        createRegistry(tableUsuario, 1, "Ricardo");
        createRegistry(tableUsuario, 2, "Daniel");

        GreaterValueComparator greaterValueComparator = new GreaterValueComparator(new Integer(1), usuarioTableDef.getColumnDef("cod"));
        greaterValueComparator.setOrder(0);
        tableUsuario.tableComparators.add(greaterValueComparator);

        TableJoinRegistry tableEmpresa = new TableJoinRegistry();
        tableEmpresa.tableDef = empresaTableDef;
        createRegistry(tableEmpresa, 1, "Senior");
        createRegistry(tableEmpresa, 2, "TSystems");

        TableJoinRegistry[] tables = new TableJoinRegistry[] { tableUsuario, tableEmpresa };

        ArrayList<AbstractBooleanComparator> whereRelationalsOperators = new ArrayList<AbstractBooleanComparator>();

        List<IRegistry> registrysJoined = JoinUtils.joinTables(whereRelationalsOperators, tables);

        Assert.assertEquals(2, registrysJoined.size());

        Assert.assertEquals(2, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    @Test
    public void testJoin005() {
        IDatabaseDef database = new DatabaseMock();
        ITableDef empresaTableDef = database.getTableDef("empresa");
        ITableDef usuarioTableDef = database.getTableDef("usuario");

        TableJoinRegistry tableUsuario = new TableJoinRegistry();
        tableUsuario.tableDef = usuarioTableDef;
        createRegistry(tableUsuario, 1, "Ricardo");
        createRegistry(tableUsuario, 2, "Daniel");

        GreaterOrEqualsValueComparator greaterValueComparator = new GreaterOrEqualsValueComparator(new Integer(1), usuarioTableDef.getColumnDef("cod"), null);
        greaterValueComparator.setOrder(0);
        tableUsuario.tableComparators.add(greaterValueComparator);

        TableJoinRegistry tableEmpresa = new TableJoinRegistry();
        tableEmpresa.tableDef = empresaTableDef;
        createRegistry(tableEmpresa, 1, "Senior");
        createRegistry(tableEmpresa, 2, "TSystems");

        TableJoinRegistry[] tables = new TableJoinRegistry[] { tableUsuario, tableEmpresa };

        ArrayList<AbstractBooleanComparator> whereRelationalsOperators = new ArrayList<AbstractBooleanComparator>();

        List<IRegistry> registrysJoined = JoinUtils.joinTables(whereRelationalsOperators, tables);

        Assert.assertEquals(4, registrysJoined.size());

        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    @Test
    public void testJoin006() {
        IDatabaseDef database = new DatabaseMock();
        ITableDef empresaTableDef = database.getTableDef("empresa");
        ITableDef usuarioTableDef = database.getTableDef("usuario");

        TableJoinRegistry tableUsuario = new TableJoinRegistry();
        tableUsuario.tableDef = usuarioTableDef;
        createRegistry(tableUsuario, 1, "Ricardo");
        createRegistry(tableUsuario, 2, "Daniel");

        LessOrEqualsValueComparator lessValueComparator = new LessOrEqualsValueComparator(new Integer(2), usuarioTableDef.getColumnDef("cod"), null);
        lessValueComparator.setOrder(0);
        tableUsuario.tableComparators.add(lessValueComparator);

        TableJoinRegistry tableEmpresa = new TableJoinRegistry();
        tableEmpresa.tableDef = empresaTableDef;
        createRegistry(tableEmpresa, 1, "Senior");
        createRegistry(tableEmpresa, 2, "TSystems");

        TableJoinRegistry[] tables = new TableJoinRegistry[] { tableUsuario, tableEmpresa };

        ArrayList<AbstractBooleanComparator> whereRelationalsOperators = new ArrayList<AbstractBooleanComparator>();

        List<IRegistry> registrysJoined = JoinUtils.joinTables(whereRelationalsOperators, tables);

        Assert.assertEquals(4, registrysJoined.size());

        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    @Test
    public void testJoin004() {
        IDatabaseDef database = new DatabaseMock();
        ITableDef empresaTableDef = database.getTableDef("empresa");
        ITableDef usuarioTableDef = database.getTableDef("usuario");

        TableJoinRegistry tableUsuario = new TableJoinRegistry();
        tableUsuario.tableDef = usuarioTableDef;
        createRegistry(tableUsuario, 1, "Ricardo");
        createRegistry(tableUsuario, 2, "Daniel");

        LessValueComparator lessValueComparator = new LessValueComparator(new Integer(2), usuarioTableDef.getColumnDef("cod"));
        lessValueComparator.setOrder(0);
        tableUsuario.tableComparators.add(lessValueComparator);

        TableJoinRegistry tableEmpresa = new TableJoinRegistry();
        tableEmpresa.tableDef = empresaTableDef;
        createRegistry(tableEmpresa, 1, "Senior");
        createRegistry(tableEmpresa, 2, "TSystems");

        TableJoinRegistry[] tables = new TableJoinRegistry[] { tableUsuario, tableEmpresa };

        ArrayList<AbstractBooleanComparator> whereRelationalsOperators = new ArrayList<AbstractBooleanComparator>();

        List<IRegistry> registrysJoined = JoinUtils.joinTables(whereRelationalsOperators, tables);

        Assert.assertEquals(2, registrysJoined.size());

        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    @Test
    public void testJoin007() {
        IDatabaseDef database = new DatabaseMock();
        ITableDef empresaTableDef = database.getTableDef("empresa");
        ITableDef usuarioTableDef = database.getTableDef("usuario");

        TableJoinRegistry tableUsuario = new TableJoinRegistry();
        tableUsuario.tableDef = usuarioTableDef;
        createRegistry(tableUsuario, 1, "Ricardo");
        createRegistry(tableUsuario, 2, "Daniel");

        EqualsValueComparator equalsValueComparator = new EqualsValueComparator(null, usuarioTableDef.getColumnDef("cod"), usuarioTableDef.getColumnDef("cod"));
        equalsValueComparator.setOrder(0);
        tableUsuario.tableComparators.add(equalsValueComparator);

        TableJoinRegistry tableEmpresa = new TableJoinRegistry();
        tableEmpresa.tableDef = empresaTableDef;
        createRegistry(tableEmpresa, 1, "Senior");
        createRegistry(tableEmpresa, 2, "TSystems");

        TableJoinRegistry[] tables = new TableJoinRegistry[] { tableUsuario, tableEmpresa };

        ArrayList<AbstractBooleanComparator> whereRelationalsOperators = new ArrayList<AbstractBooleanComparator>();

        List<IRegistry> registrysJoined = JoinUtils.joinTables(whereRelationalsOperators, tables);

        Assert.assertEquals(4, registrysJoined.size());

        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(0).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(1, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Ricardo", registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(1).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(1, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("Senior", registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(2).columnValue.get(empresaTableDef.getColumnDef("caractere")));

        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("cod")));
        Assert.assertEquals("Daniel", registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(usuarioTableDef.getColumnDef("caractere")));
        Assert.assertEquals(2, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("cod")));
        Assert.assertEquals("TSystems", registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("nome")));
        Assert.assertEquals(null, registrysJoined.get(3).columnValue.get(empresaTableDef.getColumnDef("caractere")));
    }

    /**
     * @param tableUsuario
     */
    protected void createRegistry(TableJoinRegistry tableUsuario, int cod, String nome) {
        IRegistry registro = new IRegistry();
        registro.columnValue.put(tableUsuario.tableDef.getColumnDef("cod"), new Integer(cod));
        registro.columnValue.put(tableUsuario.tableDef.getColumnDef("nome"), nome);
        tableUsuario.registrys.add(registro);
        tableUsuario.tableDef.incrementRowsCount();
    }
}
