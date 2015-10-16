package database.command.compiler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import database.command.ICommandExecutor;
import database.command.SelectCommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;
import database.metadata.DataType;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

public class SelectCommandCompiler implements ICommandCompiler {

    private LinkedList<LinkedList<String>> selectedFields = new LinkedList<>(); // Campos selecionados (ex: codigoArea ou tabela.codigoArea)
    private List<String> selectedTables = new LinkedList<>(); // Tabelas selecionadas
    private LinkedList<WhereCondition> whereConditions = new LinkedList<>();
    private List<String> whereLogicalConditions = new LinkedList<>();

    private boolean isCompilingWhere = false;

    private enum WhereDataType {
        NUMBER, LITERAL, NULL
    }

    // Mapeamento dos tipos que podem ocorrer no where, com os tipos de campos que existem
    private static HashMap<DataType, List<WhereDataType>> dataTypeToWhereDataTypeMap = new HashMap<>();
    {
        LinkedList<WhereDataType> typeList = new LinkedList<>();
        typeList.add(WhereDataType.LITERAL);
        typeList.add(WhereDataType.NULL);
        dataTypeToWhereDataTypeMap.put(DataType.CHAR, typeList);
        dataTypeToWhereDataTypeMap.put(DataType.VARCHAR, typeList);

        typeList = new LinkedList<>();
        typeList.add(WhereDataType.NUMBER);
        typeList.add(WhereDataType.NULL);
        dataTypeToWhereDataTypeMap.put(DataType.NUMBER, typeList);
    }

    private class WhereCondition {

        public String right;
        public LinkedList<String> leftField;
        public String relationalOperation;
        public WhereDataType dataType;
    }

    @Override
    public void accept(int action, Token token) {
        System.out.println("SELECT_COMMAND_COMPILER - Ação #" + action + ", Token: " + token);
        switch (action) {
            case 10:
                whereConditions.getLast().dataType = WhereDataType.NUMBER;
                whereConditions.getLast().right = token.getLexeme();
            case 11:
                whereConditions.getLast().dataType = WhereDataType.LITERAL;
                whereConditions.getLast().right = token.getLexeme();
            case 12:
                whereConditions.getLast().dataType = WhereDataType.NULL;
                whereConditions.getLast().right = token.getLexeme();
                break;
            case 40:
                if (isCompilingWhere) {
                    WhereCondition wc = new WhereCondition();
                    wc.leftField = new LinkedList<>();
                    wc.leftField.add(token.getLexeme());
                    whereConditions.add(wc);
                } else {
                    LinkedList<String> field = new LinkedList<String>();
                    field.add(token.getLexeme());

                    selectedFields.add(field);
                }
                break;
            case 41:
            case 42:
                if (isCompilingWhere) {
                    whereConditions.getLast().leftField.add(token.getLexeme());
                } else {
                    selectedFields.getLast().add(token.getLexeme());
                }
                break;
            case 43:
                break;
            case 44:
                break;
            case 45:
                isCompilingWhere = true;
                break;
            case 46:
                selectedTables.add(token.getLexeme());
                break;
            case 47:
                break;
            case 48:
                break;
            case 49:
                whereConditions.getLast().relationalOperation = token.getLexeme();
                break;
            case 50:
                whereLogicalConditions.add(token.getLexeme());
                break;
        }
    }

    @Override
    public ICommandExecutor compile() throws SemanticError {
        IDatabaseDef database = DatabaseManager.getInstance().getDatabase();

        System.out.println("-- Conferindo campos selecionados --");
        for (LinkedList<String> field : selectedFields) {
            checkSelectedField(database, field);
        }

        System.out.println("-- Conferindo tabelas selecionadas --");
        for (String table : selectedTables) {
            checkTable(database, table);
        }

        System.out.println("-- Conferindo cláusula where --");
        for (WhereCondition whereCondition : whereConditions) {
            checkWhere(database, whereCondition);
        }

        return new SelectCommandExecutor();
    }

    private IColumnDef checkSelectedField(IDatabaseDef database, LinkedList<String> tableAndField) throws SemanticError {
        IColumnDef columnDef = null;
        if (tableAndField.size() > 1) { // Se está estruturado em tabela.coluna
            String table = tableAndField.getFirst();
            String field = tableAndField.getLast();
            if (!selectedTables.contains(table)) {
                throw new SemanticError("Tabela " + table + " não está presente na cláusula FROM");
            }
            ITableDef tableDef = checkTable(database, table);
            columnDef = tableDef.getColumnDef(field);
            if (columnDef == null) {
                throw new SemanticError("Tabela " + table + " não possuí nenhuma coluna " + field);
            }
            return columnDef;
        } else {
            if (selectedTables.size() > 1) {
                throw new SemanticError("Você deve informar a tabela para a coluna " + tableAndField.getFirst());
            }
            ITableDef tableDef = checkTable(database, selectedTables.get(0));
            columnDef = tableDef.getColumnDef(tableAndField.getFirst());
            if (columnDef == null) {
                throw new SemanticError("Tabela " + selectedTables.get(0) + " não possuí nenhuma coluna " + tableAndField.getFirst());
            }
        }
        return columnDef;
    }

    private ITableDef checkTable(IDatabaseDef database, String tableName) throws SemanticError {
        ITableDef tableDef = database.getTableDef(tableName);
        if (tableDef == null) {
            throw new SemanticError("Tabela " + tableName + " não existe");
        }
        return tableDef;
    }

    private void checkWhere(IDatabaseDef database, WhereCondition whereCondition) throws SemanticError {
        IColumnDef columnDef = checkSelectedField(database, whereCondition.leftField);

        if (!dataTypeToWhereDataTypeMap.get(columnDef.getDataType()).contains(whereCondition.dataType)) {
            throw new SemanticError("Tipos incompatíveis, " + columnDef.getDataType() + " e " + whereCondition.dataType);
        }
    }

}
