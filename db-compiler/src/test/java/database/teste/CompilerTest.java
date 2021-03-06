/*
 * Created on 07/10/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import database.command.DropTableCommandExecutor;
import database.command.ICommandExecutor;
import database.command.SelectCommandExecutor;
import database.gals.LexicalError;
import database.gals.Lexico;
import database.gals.SemanticError;
import database.gals.Semantico;
import database.gals.Sintatico;
import database.gals.SyntaticError;
import database.manager.DatabaseManager;
import database.metadata.interfaces.ITableDef;
import database.utils.EqualsValueComparator;

/**
 * @author ricardo.reiter
 */
public class CompilerTest {

    private static List<ICommandExecutor> compile(String sql) throws LexicalError, SyntaticError, SemanticError {
        DatabaseManager.INSTANCE.addDatabase("DatabaseTeste", new DatabaseMock());
        DatabaseManager.INSTANCE.setDatabase("DatabaseTeste");
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
            List<ICommandExecutor> commandExecutor = compile("SELECT usuario.cod, empresa.naoexiste FROM usuario, empresa;");
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
        List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.nome = \"teste\";");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(6, executor.getSelectedColumns().size());
        Assert.assertEquals("cod", executor.getSelectedColumns().get(0).getName());
        Assert.assertEquals("nome", executor.getSelectedColumns().get(1).getName());
        Assert.assertEquals("caractere", executor.getSelectedColumns().get(2).getName());
        Assert.assertEquals("cod", executor.getSelectedColumns().get(3).getName());
        Assert.assertEquals("nome", executor.getSelectedColumns().get(4).getName());
        Assert.assertEquals("caractere", executor.getSelectedColumns().get(5).getName());

        Assert.assertEquals(2, executor.getTableList().size());
        Assert.assertEquals("usuario", executor.getTableList().get(0).getName());
        Assert.assertEquals("empresa", executor.getTableList().get(1).getName());

        Assert.assertEquals(0, executor.getWhereConditionsLogicalOperators().size());
    }

    @Test
    public void testSelectWhereCompile002() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = 12;");
    }

    @Test
    public void testSelectWhereCompile003() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = 12 AND usuario.nome = \"Nomeloko\" OR usuario.cod = 10;");
        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(3, executor.getTableComparators().get(DatabaseManager.INSTANCE.getActualDatabase().getTableDef("usuario")).size());
    }

    @Test
    public void testSelectWhereCompile004() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = empresa.cod;");
    }

    @Test
    public void testSelectWhereCompile005() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = empresa.cod AND empresa.cod = 10;");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);

        ITableDef empresaDef = DatabaseManager.INSTANCE.getActualDatabase().getTableDef("empresa");
        ITableDef usuarioDef = DatabaseManager.INSTANCE.getActualDatabase().getTableDef("usuario");
        Assert.assertNotNull(executor.getTableJoinComparators().get(empresaDef));
        Assert.assertNotNull(executor.getTableJoinComparators().get(empresaDef).get(usuarioDef));
        Assert.assertEquals(1, executor.getTableJoinComparators().get(empresaDef).get(usuarioDef).size());
        Assert.assertEquals(0, executor.getTableJoinComparators().get(empresaDef).get(usuarioDef).get(0).getOrder());
        Assert.assertEquals("cod", executor.getTableJoinComparators().get(empresaDef).get(usuarioDef).get(0).getColumnLeft().getName());
        Assert.assertEquals("cod", executor.getTableJoinComparators().get(empresaDef).get(usuarioDef).get(0).getColumnRight().getName());

        Assert.assertNotNull(executor.getTableJoinComparators().get(usuarioDef));
        Assert.assertNotNull(executor.getTableJoinComparators().get(usuarioDef).get(empresaDef));
        Assert.assertEquals(1, executor.getTableJoinComparators().get(usuarioDef).get(empresaDef).size());
        Assert.assertEquals(0, executor.getTableJoinComparators().get(usuarioDef).get(empresaDef).get(0).getOrder());
        Assert.assertEquals("cod", executor.getTableJoinComparators().get(usuarioDef).get(empresaDef).get(0).getColumnLeft().getName());
        Assert.assertEquals("cod", executor.getTableJoinComparators().get(usuarioDef).get(empresaDef).get(0).getColumnRight().getName());
    }

    @Test
    public void testSelectWhereCompile006() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.* FROM usuario WHERE nome = \"teste\";");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(3, executor.getSelectedColumns().size());
        Assert.assertEquals("cod", executor.getSelectedColumns().get(0).getName());
        Assert.assertEquals("nome", executor.getSelectedColumns().get(1).getName());
        Assert.assertEquals("caractere", executor.getSelectedColumns().get(2).getName());

        Assert.assertEquals(1, executor.getTableList().size());
        Assert.assertEquals("usuario", executor.getTableList().get(0).getName());

        Assert.assertEquals(0, executor.getWhereConditionsLogicalOperators().size());

        Assert.assertEquals(1, executor.getTableComparators().get(executor.getTableList().get(0)).size());
        Assert.assertTrue(executor.getTableComparators().get(executor.getTableList().get(0)).get(0) instanceof EqualsValueComparator);
    }

    @Test
    public void testSelectWhereCompile007() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.* FROM usuario WHERE nome = nome;");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(3, executor.getSelectedColumns().size());
        Assert.assertEquals("cod", executor.getSelectedColumns().get(0).getName());
        Assert.assertEquals("nome", executor.getSelectedColumns().get(1).getName());
        Assert.assertEquals("caractere", executor.getSelectedColumns().get(2).getName());

        Assert.assertEquals(1, executor.getTableList().size());
        Assert.assertEquals("usuario", executor.getTableList().get(0).getName());

        Assert.assertEquals(0, executor.getWhereConditionsLogicalOperators().size());

        Assert.assertEquals(1, executor.getTableComparators().get(executor.getTableList().get(0)).size());
        Assert.assertTrue(executor.getTableComparators().get(executor.getTableList().get(0)).get(0) instanceof EqualsValueComparator);
        Assert.assertEquals("nome", executor.getTableComparators().get(executor.getTableList().get(0)).get(0).getColumnRight().getName());
        Assert.assertEquals("nome", executor.getTableComparators().get(executor.getTableList().get(0)).get(0).getColumnLeft().getName());
    }

    @Test
    public void testSelectWhereCompile008() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.* FROM usuario WHERE nome = \"teste\" or nome = \"juca\";");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(3, executor.getSelectedColumns().size());
        Assert.assertEquals("cod", executor.getSelectedColumns().get(0).getName());
        Assert.assertEquals("nome", executor.getSelectedColumns().get(1).getName());
        Assert.assertEquals("caractere", executor.getSelectedColumns().get(2).getName());

        Assert.assertEquals(1, executor.getTableList().size());
        Assert.assertEquals("usuario", executor.getTableList().get(0).getName());

        Assert.assertEquals(1, executor.getWhereConditionsLogicalOperators().size());

        Assert.assertEquals(2, executor.getTableComparators().get(executor.getTableList().get(0)).size());
        Assert.assertTrue(executor.getTableComparators().get(executor.getTableList().get(0)).get(0) instanceof EqualsValueComparator);
        Assert.assertEquals("nome", executor.getTableComparators().get(executor.getTableList().get(0)).get(0).getColumnLeft().getName());
        Assert.assertEquals(null, executor.getTableComparators().get(executor.getTableList().get(0)).get(0).getColumnRight());

        Assert.assertEquals("nome", executor.getTableComparators().get(executor.getTableList().get(0)).get(1).getColumnLeft().getName());
        Assert.assertEquals(null, executor.getTableComparators().get(executor.getTableList().get(0)).get(1).getColumnRight());

        Assert.assertEquals(0, executor.getTablesForceLoadAll().size());
    }

    @Test
    public void testSelectWhereCompile009() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.* FROM usuario, empresa WHERE usuario.nome = \"teste\" or usuario.nome = \"juca\";");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(1, executor.getTablesForceLoadAll().size());
        Assert.assertEquals("empresa", executor.getTablesForceLoadAll().iterator().next().getName());
    }

    @Test
    public void testSelectWhereCompile010() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.* FROM usuario, empresa WHERE usuario.nome = \"teste\" and empresa.nome = \"teste\";");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(0, executor.getTablesForceLoadAll().size());
    }

    @Test
    public void testSelectWhereCompile011() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.* FROM usuario, empresa WHERE usuario.nome = \"teste\" or empresa.nome = \"teste\";");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(2, executor.getTablesForceLoadAll().size());
        Object[] tables = executor.getTablesForceLoadAll().toArray();
        Arrays.sort(tables);
        Assert.assertEquals("empresa", ((ITableDef) tables[0]).getName());
        Assert.assertEquals("usuario", ((ITableDef) tables[1]).getName());
    }

    @Test
    public void testSelectWhereCompile012() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("SELECT usuario.* FROM usuario, empresa WHERE usuario.nome = \"teste\" and empresa.nome = \"teste\" or usuario.nome = \"teste\" and empresa.nome = \"teste\" or usuario.cod = 1;");

        SelectCommandExecutor executor = (SelectCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals(1, executor.getTablesForceLoadAll().size());
        Assert.assertEquals("empresa", executor.getTablesForceLoadAll().iterator().next().getName());
    }

    @Test
    public void testSelectWhereCompile_Error001() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = empresa.nome;");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tipos incompatíveis, INTEGER e VARCHAR", e.getMessage());
        }
    }

    @Test
    public void testSelectWhereCompile_Error002() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = usuario.nome;");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tipos incompatíveis, INTEGER e VARCHAR", e.getMessage());
        }
    }

    @Test
    public void testSelectWhereCompile_Error004() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.* = 10;");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Não é possível utilizar a seleção de todos os campos '*', é necessário especificar um campo", e.getMessage());
        }
    }

    @Test
    public void testSelectWhereCompile_Error005() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = \"teste\";");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tipos incompatíveis, INTEGER e LITERAL", e.getMessage());
        }
    }

    @Test
    public void testSelectWhereCompile_Error006() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("SELECT empresa.*, usuario.* FROM usuario, empresa WHERE usuario.cod = \"teste\";");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tipos incompatíveis, INTEGER e LITERAL", e.getMessage());
        }
    }

    @Test
    public void testInsert001() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("INSERT INTO empresa (cod, nome) VALUES (1, \"Ricardo Reiter\");");
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
        List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (nome, caractere) VALUES (\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\", \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\");");
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
            Assert.assertEquals("Quantidade de campos informados é diferente da quantidade de valores informados", e.getMessage());
        }
    }

    @Test
    public void testInsert_Error004() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (nome, cod) VALUES (NULL, \"Teste\");");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tipo [LITERAL] incompatível com o campo [cod]. Era esperado um [INTEGER]", e.getMessage());
        }
    }

    @Test
    public void testInsert_Error005() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (nome, cod) VALUES (1, \"Teste\");");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tipo [INTEGER] incompatível com o campo [nome]. Era esperado um [VARCHAR]", e.getMessage());
        }
    }

    @Test
    public void testInsert_Error006() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (nome, cod) VALUES (\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\", 1);");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tamanho do VARCHAR informado diferente do tamanho do campo, era esperando um VARCHAR com até [100] caracteres, mas encontrou [102]", e.getMessage());
        }
    }

    @Test
    public void testInsert_Error007() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (caractere, cod) VALUES (\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\", 1);");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tamanho do CHARACTER informado diferente do tamanho do campo, era esperando um CHARACTER com [100] caracteres, mas encontrou [102]", e.getMessage());
        }
    }

    @Test
    public void testInsert_Error008() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (caractere, cod) VALUES (\"sasd\", 1);");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tamanho do CHARACTER informado diferente do tamanho do campo, era esperando um CHARACTER com [100] caracteres, mas encontrou [4]", e.getMessage());
        }
    }

    @Test
    public void testInsert_Error009() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (caractere, cod) VALUES (1, 1);");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tipo [INTEGER] incompatível com o campo [caractere]. Era esperado um [CHAR]", e.getMessage());
        }
    }

    @Test
    public void testInsert_Error010() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("INSERT INTO usuario (nome, nome) VALUES (\"teste\", \"teste\");");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Campo [nome] já declarado", e.getMessage());
        }
    }

    @Test
    public void testDropTableCompile001() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("DROP TABLE usuario;");

        DropTableCommandExecutor executor = (DropTableCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals("usuario", executor.getTable().getName());
    }

    @Test
    public void testDropTableCompile002() throws LexicalError, SyntaticError, SemanticError {
        List<ICommandExecutor> commandExecutor = compile("DROP TABLE empresa;");

        DropTableCommandExecutor executor = (DropTableCommandExecutor) commandExecutor.get(0);
        Assert.assertEquals("empresa", executor.getTable().getName());
    }

    @Test
    public void testDropTableCompile_Error001() throws LexicalError, SyntaticError, SemanticError {
        try {
            List<ICommandExecutor> commandExecutor = compile("DROP TABLE naoexiste;");
            Assert.fail("Deveria dar erro");
        } catch (SemanticError e) {
            Assert.assertEquals("Tabela [naoexiste] não existe", e.getMessage());
        }
    }

}
