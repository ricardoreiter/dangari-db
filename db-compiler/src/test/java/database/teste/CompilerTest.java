/*
 * Created on 07/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.util.List;

import org.junit.Test;

import database.command.ICommandExecutor;
import database.gals.LexicalError;
import database.gals.Lexico;
import database.gals.SemanticError;
import database.gals.Semantico;
import database.gals.Sintatico;
import database.gals.SyntaticError;
import database.manager.DatabaseManager;
import junit.framework.Assert;

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
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Você deve informar a tabela para a coluna cod", e.getMessage());
		}
	}

	@Test
	public void testSelectCompile003() throws LexicalError, SyntaticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("SELECT empresa.cod, empresa.nome FROM usuario;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tabela empresa não está presente na cláusula FROM", e.getMessage());
		}
	}

	@Test
	public void testSelectCompile004() throws LexicalError, SyntaticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("SELECT cod, naoexiste FROM usuario;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tabela usuario não possuí nenhuma coluna naoexiste", e.getMessage());
		}
	}

	@Test
	public void testSelectCompile005() throws LexicalError, SyntaticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"SELECT usuario.cod, empresa.naoexiste FROM usuario, empresa;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tabela empresa não possuí nenhuma coluna naoexiste", e.getMessage());
		}
	}

	@Test
	public void testSelectCompile006() throws LexicalError, SyntaticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("SELECT usuario.asd FROM usuario;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tabela usuario não possuí nenhuma coluna asd", e.getMessage());
		}
	}

	@Test
	public void testSelectCompile007() throws LexicalError, SyntaticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("SELECT cod, empresa FROM empresa;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tabela empresa não possuí nenhuma coluna empresa", e.getMessage());
		}
	}

	@Test
	public void testSelectCompile008() throws LexicalError, SyntaticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("SELECT cod, nome FROM tabelanaoexistente;");
			Assert.fail("Deveria dar erro");
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

	@Test
	public void testSelectCompile013() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile("SELECT usuario.*, usuario.nome FROM usuario, empresa;");
		// TODO: Checar se o executor vem com os campos e tabelas corretos
	}

	@Test
	public void testSelectCompile014() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("SELECT usuario.*, usuario.* FROM usuario, empresa;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Campo usuario.* já declarado", e.getMessage());
		}
	}

	@Test
	public void testSelectCompile015() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa;");
	}

	@Test
	public void testSelectWhereCompile001() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile(
				"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.nome = \"teste\";");
	}

	@Test
	public void testSelectWhereCompile002() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile(
				"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = 12;");
	}

	@Test
	public void testSelectWhereCompile003() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile(
				"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = 12 AND usuario.nome = \"Nomeloko\" OR usuario.cod = 10;");
	}

	@Test
	public void testSelectWhereCompile004() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile(
				"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = empresa.cod;");
	}

	@Test
	public void testSelectWhereCompile005() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile(
				"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = empresa.cod AND empresa.cod = 10;");
	}

	@Test
	public void testSelectWhereCompile_Error001() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = empresa.nome;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tipos incompatíveis, INTEGER e VARCHAR", e.getMessage());
		}
	}

	@Test
	public void testSelectWhereCompile_Error002() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = usuario.nome;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tipos incompatíveis, INTEGER e VARCHAR", e.getMessage());
		}
	}

	@Test
	public void testSelectWhereCompile_Error003() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = usuario.cod;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Campo usuario.cod já declarado", e.getMessage());
		}
	}

	@Test
	public void testSelectWhereCompile_Error004() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.* = 10;");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals(
					"Não é possível utilizar a seleção de todos os campos '*', é necessário especificar um campo",
					e.getMessage());
		}
	}

	@Test
	public void testSelectWhereCompile_Error005() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = \"teste\";");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tipos incompatíveis, INTEGER e LITERAL", e.getMessage());
		}
	}

	@Test
	public void testSelectWhereCompile_Error006() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = \"teste\";");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tipos incompatíveis, INTEGER e LITERAL", e.getMessage());
		}
	}

	@Test
	public void testInsert001() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile(
				"INSERT INTO empresa (cod, nome) VALUES (1, \"Ricardo Reiter\");");
	}

	@Test
	public void testInsert002() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (cod) VALUES (2);");
	}

	@Test
	public void testInsert003() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile("INSERT INTO empresa (nome) VALUES (NULL);");
	}

	@Test
	public void testInsert004() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (cod) VALUES (NULL);");
	}

	@Test
	public void testInsert005() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile(
				"INSERT INTO usuario (nome, caractere) VALUES (\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\", \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\");");
	}

	@Test
	public void testInsert006() throws LexicalError, SyntaticError, SemanticError {
		List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (caractere) VALUES (NULL);");
	}

	@Test
	public void testInsert_Error001() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("INSERT INTO tabelaNaoExistente (cod) VALUES (NULL);");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tabela [tabelaNaoExistente] não existe", e.getMessage());
		}
	}

	@Test
	public void testInsert_Error002() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (codigo) VALUES (NULL);");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Campo [codigo] não existe na tabela [usuario]", e.getMessage());
		}
	}

	@Test
	public void testInsert_Error003() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (nome, cod) VALUES (NULL);");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Quantidade de campos informados é diferente da quantidade de valores informados",
					e.getMessage());
		}
	}

	@Test
	public void testInsert_Error004() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"INSERT INTO usuario (nome, cod) VALUES (NULL, \"Teste\");");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tipo [LITERAL] incompatível com o campo [cod]. Era esperado um [INTEGER]",
					e.getMessage());
		}
	}

	@Test
	public void testInsert_Error005() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (nome, cod) VALUES (1, \"Teste\");");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tipo [INTEGER] incompatível com o campo [nome]. Era esperado um [VARCHAR]",
					e.getMessage());
		}
	}

	@Test
	public void testInsert_Error006() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"INSERT INTO usuario (nome, cod) VALUES (\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\", 1);");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals(
					"Tamanho do VARCHAR informado diferente do tamanho do campo, era esperando um VARCHAR com até [100] caracteres, mas encontrou [102]",
					e.getMessage());
		}
	}

	@Test
	public void testInsert_Error007() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"INSERT INTO usuario (caractere, cod) VALUES (\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\", 1);");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals(
					"Tamanho do CHARACTER informado diferente do tamanho do campo, era esperando um CHARACTER com [100] caracteres, mas encontrou [102]",
					e.getMessage());
		}
	}

	@Test
	public void testInsert_Error008() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile(
					"INSERT INTO usuario (caractere, cod) VALUES (\"sasd\", 1);");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals(
					"Tamanho do CHARACTER informado diferente do tamanho do campo, era esperando um CHARACTER com [100] caracteres, mas encontrou [4]",
					e.getMessage());
		}
	}

	@Test
	public void testInsert_Error009() throws LexicalError, SyntaticError, SemanticError {
		try {
			List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (caractere, cod) VALUES (1, 1);");
			Assert.fail("Deveria dar erro");
		} catch (SemanticError e) {
			Assert.assertEquals("Tipo [INTEGER] incompatível com o campo [caractere]. Era esperado um [CHAR]",
					e.getMessage());
		}
	}

}
