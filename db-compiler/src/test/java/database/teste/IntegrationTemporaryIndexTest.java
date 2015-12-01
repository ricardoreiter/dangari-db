/*
 * Created on 10/11/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.teste;

import org.junit.Before;

import database.manager.DatabaseManager;
import database.storage.FileManager;
import database.utils.JoinUtils;

/**
 * @author ricardo.reiter
 */
public class IntegrationTemporaryIndexTest extends IntegrationIndexTest {

    @Before
    @Override
    public void beforeTest() {
        JoinUtils.createTemporaryIndex = true;
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

}
