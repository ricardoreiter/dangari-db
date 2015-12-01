/*
 * Created on 10/11/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import database.command.CommandResult;
import database.manager.DatabaseManager;
import database.storage.FileManager;
import database.utils.JoinUtils;

/**
 * @author ricardo.reiter
 */
public class IntegrationIndexTest extends IntegrationTest {

    @Before
    @Override
    public void beforeTest() {
        JoinUtils.createTemporaryIndex = false;
        if (FileManager.getDatabase("DatabaseTesteIntegration") != null) {
            FileManager.deleteDatabase("DatabaseTesteIntegration");
            DatabaseManager.INSTANCE.getDatabases().remove("DatabaseTesteIntegration");
        }
        compileAndExecute("CREATE DATABASE DatabaseTesteIntegration;");
        compileAndExecute("SET DATABASE DatabaseTesteIntegration;");

        compileAndExecute("CREATE TABLE usuario (cod INTEGER, codEmp INTEGER, codCargo INTEGER, nome VARCHAR(50), cpf CHAR(8));");
        compileAndExecute("CREATE INDEX a ON usuario (cod INTEGER, codEmp INTEGER, codCargo INTEGER, nome VARCHAR(50), cpf CHAR(8));");

        compileAndExecute("CREATE TABLE empresa (cod INTEGER, nome VARCHAR(50), uf CHAR(2));");
        compileAndExecute("CREATE INDEX a ON empresa (cod INTEGER, nome VARCHAR(50), uf CHAR(2));");

        compileAndExecute("CREATE TABLE cargo (cod INTEGER, nome VARCHAR(50));");
        compileAndExecute("CREATE INDEX a ON cargo (cod INTEGER, nome VARCHAR(50));");
    }

    /**
     * A ordem do resultado é diferente com indice
     */
    @Test
    @Override
    public void testSelect007() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, cargo.cod FROM usuario, cargo WHERE usuario.codCargo = cargo.cod AND cargo.cod >= 3 OR usuario.cod < 2;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(11, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));
        Assert.assertEquals("Ricardo fodao", columnNome.get(1));
        Assert.assertEquals("Ricardo fodao", columnNome.get(2));
        Assert.assertEquals("Ricardo fodao", columnNome.get(3));
        Assert.assertEquals("Ricardo fodao", columnNome.get(4));
        Assert.assertEquals("Ricardo fodao", columnNome.get(5));
        Assert.assertEquals("Ricardo fodao", columnNome.get(6));
        Assert.assertEquals("Ricardo fodao", columnNome.get(7));
        Assert.assertEquals("Ricardo fodao", columnNome.get(8));
        Assert.assertEquals("Gabriel", columnNome.get(9));
        Assert.assertEquals("Gabriel", columnNome.get(10));

        List<String> columnCod = result.getValues().get("cod - 2");
        Assert.assertEquals(11, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        Assert.assertEquals("1", columnCod.get(1));
        Assert.assertEquals("1", columnCod.get(2));
        Assert.assertEquals("2", columnCod.get(3));
        Assert.assertEquals("2", columnCod.get(4));
        Assert.assertEquals("2", columnCod.get(5));
        Assert.assertEquals("3", columnCod.get(6));
        Assert.assertEquals("3", columnCod.get(7));
        Assert.assertEquals("3", columnCod.get(8));
        Assert.assertEquals("3", columnCod.get(9));
        Assert.assertEquals("3", columnCod.get(10));
    }

    @Override
    @Test
    public void testSelect006() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, cargo.cod FROM usuario, cargo WHERE usuario.codCargo = cargo.cod OR usuario.codCargo <> cargo.cod;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(36, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));
        Assert.assertEquals("Daniel", columnNome.get(1));
        Assert.assertEquals("Juca", columnNome.get(2));
        Assert.assertEquals("Ricardo fodao", columnNome.get(3));
        Assert.assertEquals("Daniel", columnNome.get(4));
        Assert.assertEquals("Gabriel", columnNome.get(5));
        Assert.assertEquals("Ricardo fodao", columnNome.get(6));
        Assert.assertEquals("Daniel", columnNome.get(7));
        Assert.assertEquals("Juca", columnNome.get(8));
        Assert.assertEquals("Juca", columnNome.get(9));
        Assert.assertEquals("Gabriel", columnNome.get(10));
        Assert.assertEquals("Gabriel", columnNome.get(11));

        List<String> columnCod = result.getValues().get("cod - 2");
        Assert.assertEquals(36, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        Assert.assertEquals("1", columnCod.get(1));
        Assert.assertEquals("1", columnCod.get(2));
        Assert.assertEquals("1", columnCod.get(3));
        Assert.assertEquals("1", columnCod.get(4));
        Assert.assertEquals("1", columnCod.get(5));
        Assert.assertEquals("1", columnCod.get(6));
        Assert.assertEquals("1", columnCod.get(7));
        Assert.assertEquals("1", columnCod.get(8));
        Assert.assertEquals("1", columnCod.get(9));
        Assert.assertEquals("1", columnCod.get(10));
        Assert.assertEquals("1", columnCod.get(11));
        Assert.assertEquals("2", columnCod.get(12));
        Assert.assertEquals("2", columnCod.get(13));
        Assert.assertEquals("2", columnCod.get(14));
        Assert.assertEquals("2", columnCod.get(15));
        Assert.assertEquals("2", columnCod.get(16));
        Assert.assertEquals("2", columnCod.get(17));
        Assert.assertEquals("2", columnCod.get(18));
        Assert.assertEquals("2", columnCod.get(19));
        Assert.assertEquals("2", columnCod.get(20));
        Assert.assertEquals("2", columnCod.get(21));
        Assert.assertEquals("2", columnCod.get(22));
        Assert.assertEquals("2", columnCod.get(23));
        Assert.assertEquals("3", columnCod.get(24));
        Assert.assertEquals("3", columnCod.get(25));
        Assert.assertEquals("3", columnCod.get(26));
        Assert.assertEquals("3", columnCod.get(27));
        Assert.assertEquals("3", columnCod.get(28));
        Assert.assertEquals("3", columnCod.get(29));
        Assert.assertEquals("3", columnCod.get(30));
        Assert.assertEquals("3", columnCod.get(31));
        Assert.assertEquals("3", columnCod.get(32));
        Assert.assertEquals("3", columnCod.get(33));
        Assert.assertEquals("3", columnCod.get(34));
        Assert.assertEquals("3", columnCod.get(35));
    }

}
