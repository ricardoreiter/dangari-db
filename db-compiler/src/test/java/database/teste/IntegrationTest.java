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
import database.command.ICommandExecutor;
import database.gals.LexicalError;
import database.gals.Lexico;
import database.gals.SemanticError;
import database.gals.Semantico;
import database.gals.Sintatico;
import database.gals.SyntaticError;
import database.manager.DatabaseManager;
import database.storage.FileManager;

/**
 * @author ricardo.reiter
 */
public class IntegrationTest {

    private CommandResult compileAndExecute(String sql) {
        Lexico lexico = new Lexico(sql);
        Sintatico sintatico = new Sintatico();

        Semantico semanticAnalyser = new Semantico();
        try {
            sintatico.parse(lexico, semanticAnalyser);
        } catch (LexicalError | SyntaticError | SemanticError e) {
            throw new RuntimeException(e);
        }

        CommandResult result = null;
        for (ICommandExecutor executor : semanticAnalyser.getExecutor()) {
            result = executor.execute();
        }

        return result;
    }

    private void insertValues() {
        compileAndExecute("INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 1, 1, \"Ricardo fodao\", \"89042100\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (2, 1, 1, \"Daniel\", \"32145678\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (3, 1, 3, \"Gabriel\", \"45612378\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (4, 1, 1, \"Juca\", \"78654684\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 2, 1, \"Ricardo fodao\", \"89042100\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (2, 2, 1, \"Daniel\", \"32145678\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (3, 2, 1, \"Gabriel\", \"45612378\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (4, 2, 2, \"Juca\", \"78654684\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 3, 1, \"Ricardo fodao\", \"89042100\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (2, 3, 1, \"Daniel\", \"32145678\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (3, 3, 3, \"Gabriel\", \"45612378\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (4, 3, 2, \"Juca\", \"78654684\");\n" + //
        "INSERT INTO empresa (cod, nome, uf) values (1, \"Furb\", \"SC\");\n" + //
        "INSERT INTO empresa (cod, nome, uf) values (2, \"Senior\", \"SC\");\n" + //
        "INSERT INTO empresa (cod, nome, uf) values (3, \"TSystems\", \"SC\");\n" + //
        "INSERT INTO cargo (cod, nome) values (1, \"Programador\");\n" + //
        "INSERT INTO cargo (cod, nome) values (2, \"Analista\");\n" + //
        "INSERT INTO cargo (cod, nome) values (3, \"Projetista\");\n");
    }

    @Before
    public void beforeTest() {
        if (FileManager.getDatabase("DatabaseTesteIntegration") != null) {
            FileManager.deleteDatabase("DatabaseTesteIntegration");
            DatabaseManager.INSTANCE.getDatabases().remove("DatabaseTesteIntegration");
        }
        compileAndExecute("CREATE DATABASE DatabaseTesteIntegration;");
        compileAndExecute("SET DATABASE DatabaseTesteIntegration;");
        compileAndExecute("CREATE TABLE usuario (cod INTEGER, codEmp INTEGER, codCargo INTEGER, nome VARCHAR(50), cpf CHAR(8));");
        compileAndExecute("CREATE TABLE empresa (cod INTEGER, nome VARCHAR(50), uf CHAR(2));");
        compileAndExecute("CREATE TABLE cargo (cod INTEGER, nome VARCHAR(50));");
    }

    @Test
    public void testInsert001() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 1, 1, \"Ricardo fodao\", \"89042100\");");
        Assert.assertEquals("Registro inserido", result.getValues().get("Info").get(0));
    }

    @Test
    public void testInsert002() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 1, 1, \"Ricardo fodao\", \"89042100\");\n" + // 
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 2, 1, \"Ricardo fodao\", \"89042100\");\n" + //
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 3, 1, \"Ricardo fodao\", \"89042100\");\n" + //
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 4, 1, \"Ricardo fodao\", \"89042100\");\n" + //
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 5, 1, \"Ricardo fodao\", \"89042100\");\n" + //
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 6, 1, \"Ricardo fodao\", \"89042100\");\n" + //
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 7, 1, \"Ricardo fodao\", \"89042100\");\n" + //
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 8, 1, \"Ricardo fodao\", \"89042100\");\n" + //
        "INSERT INTO usuario (cod, codEmp, codCargo, nome, cpf) values (1, 9, 1, \"Ricardo fodao\", \"89042100\");\n");
        Assert.assertEquals("Registro inserido", result.getValues().get("Info").get(0));
    }

    @Test
    public void testInsert003() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (cod, codCargo, nome) values (1, 1, \"Ricardo fodao\");");
        Assert.assertEquals("Registro inserido", result.getValues().get("Info").get(0));
    }

    @Test
    public void testSelect001() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, empresa.uf FROM usuario, empresa, cargo WHERE usuario.codEmp = empresa.cod AND empresa.cod = 1 AND usuario.codCargo = cargo.cod AND cargo.nome = \"Programador\";");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(3, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));
        Assert.assertEquals("Daniel", columnNome.get(1));
        Assert.assertEquals("Juca", columnNome.get(2));

        List<String> columnUF = result.getValues().get("uf - 2");
        Assert.assertEquals(3, columnUF.size());
        Assert.assertEquals("SC", columnUF.get(0));
        Assert.assertEquals("SC", columnUF.get(1));
        Assert.assertEquals("SC", columnUF.get(2));
    }

    @Test
    public void testSelect002() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome FROM usuario;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(12, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));
        Assert.assertEquals("Daniel", columnNome.get(1));
        Assert.assertEquals("Gabriel", columnNome.get(2));
        Assert.assertEquals("Juca", columnNome.get(3));
        Assert.assertEquals("Ricardo fodao", columnNome.get(4));
        Assert.assertEquals("Daniel", columnNome.get(5));
        Assert.assertEquals("Gabriel", columnNome.get(6));
        Assert.assertEquals("Juca", columnNome.get(7));
        Assert.assertEquals("Ricardo fodao", columnNome.get(8));
        Assert.assertEquals("Daniel", columnNome.get(9));
        Assert.assertEquals("Gabriel", columnNome.get(10));
        Assert.assertEquals("Juca", columnNome.get(11));
    }

}
