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
    public void testInsert_Select001() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (cod, codCargo, nome, cpf) values (1, 1, \"Ricardo fodao\", \"89042100\");");
     
        result = compileAndExecute("SELECT usuario.* FROM usuario;");

        List<String> columnCod = result.getValues().get("cod - 1");
        Assert.assertEquals(1, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        
        List<String> columnCodEmp = result.getValues().get("codEmp - 2");
        Assert.assertEquals(1, columnCodEmp.size());
        Assert.assertEquals("0", columnCodEmp.get(0));

        List<String> columnCodCargo = result.getValues().get("codCargo - 3");
        Assert.assertEquals(1, columnCodCargo.size());
        Assert.assertEquals("1", columnCodCargo.get(0));
        
        List<String> columnNome = result.getValues().get("nome - 4");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));

        List<String> columnCPF = result.getValues().get("cpf - 5");
        Assert.assertEquals(1, columnCPF.size());
        Assert.assertEquals("89042100", columnCPF.get(0));
    }
    
    @Test
    public void testInsert_Select002() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (cod, codEmp, nome, cpf) values (1, 1, \"Ricardo fodao\", \"89042100\");");
     
        result = compileAndExecute("SELECT usuario.* FROM usuario;");

        List<String> columnCod = result.getValues().get("cod - 1");
        Assert.assertEquals(1, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        
        List<String> columnCodEmp = result.getValues().get("codEmp - 2");
        Assert.assertEquals(1, columnCodEmp.size());
        Assert.assertEquals("1", columnCodEmp.get(0));

        List<String> columnCodCargo = result.getValues().get("codCargo - 3");
        Assert.assertEquals(1, columnCodCargo.size());
        Assert.assertEquals("0", columnCodCargo.get(0));
        
        List<String> columnNome = result.getValues().get("nome - 4");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));

        List<String> columnCPF = result.getValues().get("cpf - 5");
        Assert.assertEquals(1, columnCPF.size());
        Assert.assertEquals("89042100", columnCPF.get(0));
    }
    
    @Test
    public void testInsert_Select003() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (nome, cpf) values (\"Ricardo fodao\", \"89042100\");");
     
        result = compileAndExecute("SELECT usuario.* FROM usuario;");

        List<String> columnCod = result.getValues().get("cod - 1");
        Assert.assertEquals(1, columnCod.size());
        Assert.assertEquals("0", columnCod.get(0));
        
        List<String> columnCodEmp = result.getValues().get("codEmp - 2");
        Assert.assertEquals(1, columnCodEmp.size());
        Assert.assertEquals("0", columnCodEmp.get(0));

        List<String> columnCodCargo = result.getValues().get("codCargo - 3");
        Assert.assertEquals(1, columnCodCargo.size());
        Assert.assertEquals("0", columnCodCargo.get(0));
        
        List<String> columnNome = result.getValues().get("nome - 4");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));

        List<String> columnCPF = result.getValues().get("cpf - 5");
        Assert.assertEquals(1, columnCPF.size());
        Assert.assertEquals("89042100", columnCPF.get(0));
    }
    
    @Test
    public void testInsert_Select004() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (nome, cod) values (\"Ricardo fodao\", 1);");
     
        result = compileAndExecute("SELECT usuario.* FROM usuario;");

        List<String> columnCod = result.getValues().get("cod - 1");
        Assert.assertEquals(1, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        
        List<String> columnCodEmp = result.getValues().get("codEmp - 2");
        Assert.assertEquals(1, columnCodEmp.size());
        Assert.assertEquals("0", columnCodEmp.get(0));

        List<String> columnCodCargo = result.getValues().get("codCargo - 3");
        Assert.assertEquals(1, columnCodCargo.size());
        Assert.assertEquals("0", columnCodCargo.get(0));
        
        List<String> columnNome = result.getValues().get("nome - 4");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Ricardo fodao", columnNome.get(0));

        List<String> columnCPF = result.getValues().get("cpf - 5");
        Assert.assertEquals(1, columnCPF.size());
        Assert.assertEquals("", columnCPF.get(0));
    }
    
    @Test
    public void testInsert_Select005() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (codCargo, cod, cpf) values (2, 1, \"12345678\");");
     
        result = compileAndExecute("SELECT usuario.* FROM usuario;");

        List<String> columnCod = result.getValues().get("cod - 1");
        Assert.assertEquals(1, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        
        List<String> columnCodEmp = result.getValues().get("codEmp - 2");
        Assert.assertEquals(1, columnCodEmp.size());
        Assert.assertEquals("0", columnCodEmp.get(0));

        List<String> columnCodCargo = result.getValues().get("codCargo - 3");
        Assert.assertEquals(1, columnCodCargo.size());
        Assert.assertEquals("2", columnCodCargo.get(0));
        
        List<String> columnNome = result.getValues().get("nome - 4");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("", columnNome.get(0));

        List<String> columnCPF = result.getValues().get("cpf - 5");
        Assert.assertEquals(1, columnCPF.size());
        Assert.assertEquals("12345678", columnCPF.get(0));
    }
    
    @Test
    public void testInsert_Select006() {
        CommandResult result = compileAndExecute("INSERT INTO usuario (codCargo, cod) values (2, 1);");
     
        result = compileAndExecute("SELECT usuario.* FROM usuario;");

        List<String> columnCod = result.getValues().get("cod - 1");
        Assert.assertEquals(1, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        
        List<String> columnCodEmp = result.getValues().get("codEmp - 2");
        Assert.assertEquals(1, columnCodEmp.size());
        Assert.assertEquals("0", columnCodEmp.get(0));

        List<String> columnCodCargo = result.getValues().get("codCargo - 3");
        Assert.assertEquals(1, columnCodCargo.size());
        Assert.assertEquals("2", columnCodCargo.get(0));
        
        List<String> columnNome = result.getValues().get("nome - 4");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("", columnNome.get(0));

        List<String> columnCPF = result.getValues().get("cpf - 5");
        Assert.assertEquals(1, columnCPF.size());
        Assert.assertEquals("", columnCPF.get(0));
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
    
    @Test
    public void testSelect003() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome FROM usuario, empresa, cargo;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(108, columnNome.size());
    }
    
    @Test
    public void testSelect004() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome FROM usuario, empresa;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(36, columnNome.size());
    }
    
    @Test
    public void testSelect005() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, cargo.* FROM usuario, cargo WHERE usuario.codCargo = cargo.cod;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(12, columnNome.size());
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
        Assert.assertEquals(12, columnCod.size());
        Assert.assertEquals("1", columnCod.get(0));
        Assert.assertEquals("1", columnCod.get(1));
        Assert.assertEquals("1", columnCod.get(2));
        Assert.assertEquals("1", columnCod.get(3));
        Assert.assertEquals("1", columnCod.get(4));
        Assert.assertEquals("1", columnCod.get(5));
        Assert.assertEquals("1", columnCod.get(6));
        Assert.assertEquals("1", columnCod.get(7));
        Assert.assertEquals("2", columnCod.get(8));
        Assert.assertEquals("2", columnCod.get(9));
        Assert.assertEquals("3", columnCod.get(10));
        Assert.assertEquals("3", columnCod.get(11));
        
        List<String> columnName = result.getValues().get("nome - 3");
        Assert.assertEquals(12, columnName.size());
        Assert.assertEquals("Programador", columnName.get(0));
        Assert.assertEquals("Programador", columnName.get(1));
        Assert.assertEquals("Programador", columnName.get(2));
        Assert.assertEquals("Programador", columnName.get(3));
        Assert.assertEquals("Programador", columnName.get(4));
        Assert.assertEquals("Programador", columnName.get(5));
        Assert.assertEquals("Programador", columnName.get(6));
        Assert.assertEquals("Programador", columnName.get(7));
        Assert.assertEquals("Analista", columnName.get(8));
        Assert.assertEquals("Analista", columnName.get(9));
        Assert.assertEquals("Projetista", columnName.get(10));
        Assert.assertEquals("Projetista", columnName.get(11));
    }
    
    @Test
    public void testSelect006() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, cargo.cod FROM usuario, cargo WHERE usuario.codCargo = cargo.cod OR usuario.codCargo <> cargo.cod;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(36, columnNome.size());
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
    
    @Test
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
        Assert.assertEquals("Gabriel", columnNome.get(7));
        Assert.assertEquals("Ricardo fodao", columnNome.get(8));
        Assert.assertEquals("Ricardo fodao", columnNome.get(9));
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

    @Test
    public void testSelect008() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.cod FROM empresa WHERE empresa.uf = \"Teste\";");

        List<String> columnNome = result.getValues().get("cod - 1");
        Assert.assertEquals(0, columnNome.size());
    }
    
    @Test
    public void testSelect009() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.cod FROM empresa WHERE empresa.uf = \"SC\";");

        List<String> columnNome = result.getValues().get("cod - 1");
        Assert.assertEquals(3, columnNome.size());
        Assert.assertEquals("1", columnNome.get(0));
        Assert.assertEquals("2", columnNome.get(1));
        Assert.assertEquals("3", columnNome.get(2));
    }
    
    @Test
    public void testSelect010() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.cod FROM empresa WHERE empresa.uf > \"SB\";");

        List<String> columnNome = result.getValues().get("cod - 1");
        Assert.assertEquals(3, columnNome.size());
        Assert.assertEquals("1", columnNome.get(0));
        Assert.assertEquals("2", columnNome.get(1));
        Assert.assertEquals("3", columnNome.get(2));
    }
    
    @Test
    public void testSelect011() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.cod FROM empresa WHERE empresa.uf < \"SD\";");

        List<String> columnNome = result.getValues().get("cod - 1");
        Assert.assertEquals(3, columnNome.size());
        Assert.assertEquals("1", columnNome.get(0));
        Assert.assertEquals("2", columnNome.get(1));
        Assert.assertEquals("3", columnNome.get(2));
    }
    
    @Test
    public void testSelect012() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.cod FROM empresa WHERE empresa.uf <> \"SD\" AND empresa.cod >= 1;");

        List<String> columnNome = result.getValues().get("cod - 1");
        Assert.assertEquals(3, columnNome.size());
        Assert.assertEquals("1", columnNome.get(0));
        Assert.assertEquals("2", columnNome.get(1));
        Assert.assertEquals("3", columnNome.get(2));
    }
    
    @Test
    public void testSelect013() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.cod FROM empresa WHERE empresa.uf = \"SD\" OR empresa.cod >= 1;");

        List<String> columnNome = result.getValues().get("cod - 1");
        Assert.assertEquals(3, columnNome.size());
        Assert.assertEquals("1", columnNome.get(0));
        Assert.assertEquals("2", columnNome.get(1));
        Assert.assertEquals("3", columnNome.get(2));
    }
    
    @Test
    public void testSelect014() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.cod FROM empresa WHERE empresa.uf = \"SD\" AND empresa.cod >= 1;");

        List<String> columnNome = result.getValues().get("cod - 1");
        Assert.assertEquals(0, columnNome.size());
    }
    
    @Test
    public void testSelect015() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.nome FROM empresa WHERE empresa.uf = \"SD\" OR empresa.cod <= 1;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Furb", columnNome.get(0));
    }
    
    @Test
    public void testSelect016() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.nome FROM empresa WHERE empresa.uf = \"SD\" OR empresa.cod <> 2 AND empresa.cod <> 3;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Furb", columnNome.get(0));
    }
    
    @Test
    public void testSelect017() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.nome FROM empresa WHERE empresa.uf = \"SD\" OR empresa.cod <> 2 AND empresa.nome = \"Furb\";");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Furb", columnNome.get(0));
    }
    
    @Test
    public void testSelect018() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.nome FROM empresa WHERE empresa.uf = \"SD\" OR empresa.cod <> 2 or empresa.nome = \"Furb\";");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(2, columnNome.size());
        Assert.assertEquals("Furb", columnNome.get(0));
        Assert.assertEquals("TSystems", columnNome.get(1));
    }
    
    @Test
    public void testSelect019() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.nome FROM empresa WHERE empresa.uf = \"SD\" OR empresa.cod <> 2 or empresa.nome <> \"Senior\";");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(2, columnNome.size());
        Assert.assertEquals("Furb", columnNome.get(0));
        Assert.assertEquals("TSystems", columnNome.get(1));
    }
    
    @Test
    public void testSelect020() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT empresa.nome FROM empresa WHERE empresa.nome > \"TSystema\";");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("TSystems", columnNome.get(0));
    }
    
    @Test
    public void testSelect021() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, empresa.uf, cargo.nome FROM usuario, empresa, cargo WHERE usuario.codEmp = empresa.cod AND usuario.codCargo = cargo.cod AND cargo.cod >= 3 AND empresa.nome = \"Senior\";");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(0, columnNome.size());
    }
    
    @Test
    public void testSelect022() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, empresa.uf, cargo.nome FROM usuario, empresa, cargo WHERE usuario.codEmp = empresa.cod AND usuario.codCargo = cargo.cod AND cargo.cod >= 3 AND empresa.nome = \"TSystems\";");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Gabriel", columnNome.get(0));

        List<String> columnUF = result.getValues().get("uf - 2");
        Assert.assertEquals(1, columnUF.size());
        Assert.assertEquals("SC", columnUF.get(0));
        
        List<String> columnNomeCargo = result.getValues().get("nome - 3");
        Assert.assertEquals(1, columnNomeCargo.size());
        Assert.assertEquals("Projetista", columnNomeCargo.get(0));
    }
    
    @Test
    public void testSelect023() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, empresa.uf, cargo.nome FROM usuario, empresa, cargo WHERE usuario.codEmp = empresa.cod AND usuario.codCargo = cargo.cod AND cargo.cod >= 3 AND empresa.nome = \"TSystems\" OR usuario.cod = 2 AND empresa.cod = 2 AND cargo.cod = 1;");

        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(3, columnNome.size());
        Assert.assertEquals("Daniel", columnNome.get(0));
        Assert.assertEquals("Daniel", columnNome.get(1));
        Assert.assertEquals("Daniel", columnNome.get(2));

        List<String> columnUF = result.getValues().get("uf - 2");
        Assert.assertEquals(3, columnUF.size());
        Assert.assertEquals("SC", columnUF.get(0));
        Assert.assertEquals("SC", columnUF.get(1));
        Assert.assertEquals("SC", columnUF.get(2));
        
        List<String> columnNomeCargo = result.getValues().get("nome - 3");
        Assert.assertEquals(3, columnNomeCargo.size());
        Assert.assertEquals("Programador", columnNomeCargo.get(0));
        Assert.assertEquals("Programador", columnNomeCargo.get(1));
        Assert.assertEquals("Programador", columnNomeCargo.get(2));
    }
    
    @Test
    public void testSelect024() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, usuario.cod, usuario.codEmp, usuario.codCargo FROM usuario, empresa, cargo WHERE usuario.codEmp = empresa.cod AND usuario.codCargo = cargo.cod AND cargo.cod >= 3 AND empresa.nome = \"TSystems\" OR usuario.cod = 2 AND empresa.cod = 2 AND cargo.cod = 1 AND empresa.cod = usuario.codEmp AND cargo.cod = usuario.codCargo;");
// 
        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(1, columnNome.size());
        Assert.assertEquals("Daniel", columnNome.get(0));
        
        List<String> columnCodUsu = result.getValues().get("cod - 2");
        Assert.assertEquals(1, columnCodUsu.size());
        Assert.assertEquals("2", columnCodUsu.get(0));

        List<String> columnUF = result.getValues().get("codEmp - 3");
        Assert.assertEquals(1, columnUF.size());
        Assert.assertEquals("2", columnUF.get(0));
        
        List<String> columnNomeCargo = result.getValues().get("codCargo - 4");
        Assert.assertEquals(1, columnNomeCargo.size());
        Assert.assertEquals("1", columnNomeCargo.get(0));
    }
    
    @Test
    public void testSelect025() {
        insertValues();

        CommandResult result = compileAndExecute("SELECT usuario.nome, usuario.cod, usuario.codEmp, usuario.codCargo FROM usuario, empresa, cargo WHERE empresa.cod = usuario.codEmp AND usuario.codCargo = cargo.cod AND usuario.nome = \"Gabriel\" OR usuario.nome = \"Juca\" AND empresa.cod = usuario.codEmp AND usuario.codCargo = cargo.cod;");
// 
        List<String> columnNome = result.getValues().get("nome - 1");
        Assert.assertEquals(6, columnNome.size());
        Assert.assertEquals("Juca", columnNome.get(0));
        Assert.assertEquals("Gabriel", columnNome.get(1));
        Assert.assertEquals("Gabriel", columnNome.get(2));
        Assert.assertEquals("Juca", columnNome.get(3));
        Assert.assertEquals("Juca", columnNome.get(4));
        Assert.assertEquals("Gabriel", columnNome.get(5));
        
        List<String> columnCodUsu = result.getValues().get("cod - 2");
        Assert.assertEquals(6, columnCodUsu.size());
        Assert.assertEquals("4", columnCodUsu.get(0));
        Assert.assertEquals("3", columnCodUsu.get(1));
        

        List<String> columnUF = result.getValues().get("codEmp - 3");
        Assert.assertEquals(6, columnUF.size());
        Assert.assertEquals("1", columnUF.get(0));
        Assert.assertEquals("1", columnUF.get(1));
        Assert.assertEquals("2", columnUF.get(2));
        Assert.assertEquals("2", columnUF.get(3));
        Assert.assertEquals("3", columnUF.get(4));
        Assert.assertEquals("3", columnUF.get(5));
        
        List<String> columnNomeCargo = result.getValues().get("codCargo - 4");
        Assert.assertEquals(6, columnNomeCargo.size());
        Assert.assertEquals("1", columnNomeCargo.get(0));
    }
    
}
