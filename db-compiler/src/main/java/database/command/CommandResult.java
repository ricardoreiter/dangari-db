/*
 * Created on 06/11/2015.
 * 
 * Copyright 2015 Senior Ltda. All rights reserved.
 */
package database.command;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author ricardo.reiter
 */
public class CommandResult {

    private Map<String, List<String>> columnValues = new LinkedHashMap<>();

    public void addColumn(String columnName) {
        columnValues.put(columnName, new LinkedList<String>());
    }

    public void addValue(String columnName, String value) {
        columnValues.get(columnName).add(value);
    }

    public Map<String, List<String>> getValues() {
        return columnValues;
    }

}
