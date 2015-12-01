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

/**
 * @author ricardo.reiter
 */
public class IntegrationIndexTest extends IntegrationTest {

    @Before
    @Override
    public void beforeTest() {
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

}
