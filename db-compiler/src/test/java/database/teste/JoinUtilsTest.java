/*
 * Created on 03/11/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

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

        for (IRegistry registry : registrysJoined) {
            System.out.println("Registro 1");
            for (Entry<IColumnDef, Object> entry : registry.columnValue.entrySet()) {
                System.out.println("Coluna " + entry.getKey().getName() + " - " + entry.getValue());
            }
        }
    }

    /**
     * @param tableUsuario
     */
    private void createRegistry(TableJoinRegistry tableUsuario, int cod, String nome) {
        IRegistry registro = new IRegistry();
        registro.columnValue.put(tableUsuario.tableDef.getColumnDef("cod"), new Integer(cod));
        registro.columnValue.put(tableUsuario.tableDef.getColumnDef("nome"), nome);
        tableUsuario.registrys.add(registro);
    }
}
