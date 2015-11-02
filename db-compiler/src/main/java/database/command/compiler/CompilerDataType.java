/*
 * Created on 27/10/2015.
 * 
 * Copyright 2015 Reiter FODA Ltda. All rights reserved.
 */
package database.command.compiler;

import java.util.Arrays;
import java.util.List;

import database.metadata.DataType;

/**
 * @author ricardo.reiter
 */
public enum CompilerDataType {

	INTEGER(DataType.INTEGER), LITERAL(DataType.CHAR, DataType.VARCHAR), NULL(DataType.INTEGER, DataType.CHAR, DataType.VARCHAR), FIELD();

    private List<DataType> dataTypes;

    CompilerDataType(DataType... types) {
        dataTypes = Arrays.asList(types);
    }

    List<DataType> getDataTypes() {
        return dataTypes;
    }

}
