/*
 * Created on 07/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import database.command.ICommandExecutor;
import database.gals.LexicalError;
import database.gals.Lexico;
import database.gals.SemanticError;
import database.gals.Semantico;
import database.gals.Sintatico;
import database.gals.SyntaticError;
import database.manager.DatabaseManager;

/**
 * @author ricardo.reiter
 */
public class CompilerTest {

    private static List<ICommandExecutor> compile(String sql) throws LexicalError, SyntaticError, SemanticError {
        DatabaseManager.INSTANCE.setDatabase(new DatabaseMock());
        Lexico lexico = new Lexico(sql);
        Sintatico sintatico = new Sintatico();

        Semantico semanticAnalyser = new Semantico();
        sintatico.parse(lexico, semanticAnalyser);
        return semanticAnalyser.getExecutor();
    }

    @Test
    public void testSelectCompile001() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.cod, empresa.nome FROM usuario, empresa;");
        // TODO: Checar se o executor vem com os campos e tabelas corretos
    }

    @Test
    public void testSelectCompile002() throws LexicalError, SyntaticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT cod, empresa.nome FROM usuario, empresa;");
        } catch (SemanticError e) {
            Assert.assertEquals("Você deve informar a tabela para a coluna cod", e.getMessage());
        }
    }

    @Test
    public void testSelectCompile003() throws LexicalError, SyntaticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT empresa.cod, empresa.nome FROM usuario;");
        } catch (SemanticError e) {
            Assert.assertEquals("Tabela empresa não está presente na cláusula FROM", e.getMessage());
        }
    }

    @Test
    public void testSelectCompile004() throws LexicalError, SyntaticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT cod, naoexiste FROM usuario;");
        } catch (SemanticError e) {
            Assert.assertEquals("Tabela usuario não possuí nenhuma coluna naoexiste", e.getMessage());
        }
    }

    @Test
    public void testSelectCompile005() throws LexicalError, SyntaticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT usuario.cod, empresa.naoexiste FROM usuario, empresa;");
        } catch (SemanticError e) {
            Assert.assertEquals("Tabela empresa não possuí nenhuma coluna naoexiste", e.getMessage());
        }
    }

    @Test
    public void testSelectCompile006() throws LexicalError, SyntaticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT usuario.asd FROM usuario;");
        } catch (SemanticError e) {
            Assert.assertEquals("Tabela usuario não possuí nenhuma coluna asd", e.getMessage());
        }
    }

    @Test
    public void testSelectCompile007() throws LexicalError, SyntaticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT cod, empresa FROM empresa;");
        } catch (SemanticError e) {
            Assert.assertEquals("Tabela empresa não possuí nenhuma coluna empresa", e.getMessage());
        }
    }

    @Test
    public void testSelectCompile008() throws LexicalError, SyntaticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT cod, nome FROM tabelanaoexistente;");
        } catch (SemanticError e) {
            Assert.assertEquals("Tabela tabelanaoexistente não existe", e.getMessage());
        }
    }

    @Test
    public void testSelectCompile009() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.cod, empresa.cod FROM usuario, empresa;");
        // TODO: Checar se o executor vem com os campos e tabelas corretos
    }

    @Test
    public void testSelectCompile010() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.cod, usuario.nome FROM usuario;");
        // TODO: Checar se o executor vem com os campos e tabelas corretos
    }

    @Test
    public void testSelectCompile011() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.cod, nome FROM usuario;");
        // TODO: Checar se o executor vem com os campos e tabelas corretos
    }

    @Test
    public void testSelectCompile012() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.cod, usuario.nome FROM usuario, empresa;");
        // TODO: Checar se o executor vem com os campos e tabelas corretos
    }

}
