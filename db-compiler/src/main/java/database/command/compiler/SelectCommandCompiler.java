package database.command.compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import database.command.ICommandExecutor;
import database.command.SelectCommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

public class SelectCommandCompiler implements ICommandCompiler {

    private LinkedList<LinkedList<String>> selectedFields = new LinkedList<>(); // Campos selecionados (ex: codigoArea ou tabela.codigoArea)
    private List<String> selectedTables = new LinkedList<>(); // Tabelas selecionadas
    private LinkedList<WhereCondition> whereConditions = new LinkedList<>();
    private List<String> whereLogicalConditions = new LinkedList<>();

    private boolean isCompilingWhere = false;

    private class WhereCondition {

        public LinkedList<String> right;
        public LinkedList<String> left;
        public String relationalOperation;
        public CompilerDataType dataType;
    }

    @Override
    public void accept(int action, Token token) {
        System.out.println("SELECT_COMMAND_COMPILER - Ação #" + action + ", Token: " + token);
        switch (action) {
            case 10:
                whereConditions.getLast().dataType = CompilerDataType.INTEGER;
                whereConditions.getLast().right = new LinkedList<>();
                whereConditions.getLast().right.add(token.getLexeme());
                break;
            case 11:
                whereConditions.getLast().dataType = CompilerDataType.LITERAL;
                whereConditions.getLast().right = new LinkedList<>();
                whereConditions.getLast().right.add(token.getLexeme());
                break;
            case 12:
                whereConditions.getLast().dataType = CompilerDataType.NULL;
                whereConditions.getLast().right = new LinkedList<>();
                whereConditions.getLast().right.add(token.getLexeme());
                break;
            case 40:
                if (isCompilingWhere) {
                    if (whereConditions.size() > 0 && whereConditions.getLast().relationalOperation != null) {
                        whereConditions.getLast().right = new LinkedList<>();
                        whereConditions.getLast().right.add(token.getLexeme());
                        whereConditions.getLast().dataType = CompilerDataType.FIELD;
                    } else {
                        WhereCondition wc = new WhereCondition();
                        wc.left = new LinkedList<>();
                        wc.left.add(token.getLexeme());
                        whereConditions.add(wc);
                    }
                } else {
                    LinkedList<String> field = new LinkedList<String>();
                    field.add(token.getLexeme());

                    selectedFields.add(field);
                }
                break;
            case 41:
            case 42:
                if (isCompilingWhere) {
                    if (whereConditions.size() > 0 && whereConditions.getLast().relationalOperation != null) {
                        whereConditions.getLast().right.add(token.getLexeme());
                    } else {
                        whereConditions.getLast().left.add(token.getLexeme());
                    }
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
        ArrayList<String> checkedFields = new ArrayList<String>(selectedFields.size());
        for (LinkedList<String> field : selectedFields) {
            checkSelectedField(database, field, checkedFields, true);
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

    private IColumnDef checkSelectedField(IDatabaseDef database, LinkedList<String> tableAndField, ArrayList<String> checkedFields, boolean allowAllFields) throws SemanticError {
        IColumnDef columnDef = null;
        String tableField;
        if (tableAndField.size() > 1) { // Se está estruturado em tabela.coluna
            String table = tableAndField.getFirst();
            String field = tableAndField.getLast();
            tableField = table + "." + field;

            if (!selectedTables.contains(table)) {
                throw new SemanticError("Tabela " + table + " não está presente na cláusula FROM");
            }
            ITableDef tableDef = checkTable(database, table);

            if (field.equals("*")) {
                if (!allowAllFields) {
                    throw new SemanticError("Não é possível utilizar a seleção de todos os campos '*', é necessário especificar um campo");
                }
            } else {
                columnDef = tableDef.getColumnDef(field);
                if (columnDef == null) {
                    throw new SemanticError("Tabela " + table + " não possuí nenhuma coluna " + field);
                }
            }
        } else {
            if (selectedTables.size() > 1) {
                throw new SemanticError("Você deve informar a tabela para a coluna " + tableAndField.getFirst());
            }
            tableField = tableAndField.getFirst();
            ITableDef tableDef = checkTable(database, selectedTables.get(0));
            columnDef = tableDef.getColumnDef(tableField);
            if (columnDef == null) {
                throw new SemanticError("Tabela " + selectedTables.get(0) + " não possuí nenhuma coluna " + tableField);
            }
        }

        if (checkedFields.contains(tableField)) {
            throw new SemanticError("Campo " + tableField + " já declarado");
        } else {
            checkedFields.add(tableField);
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
        ArrayList<String> checkedFields = new ArrayList<String>();
        IColumnDef columnDef = checkSelectedField(database, whereCondition.left, checkedFields, false);

        if (whereCondition.dataType == CompilerDataType.FIELD) {
            IColumnDef rightColumn = checkSelectedField(database, whereCondition.right, checkedFields, false);
            if (columnDef.getDataType() != rightColumn.getDataType()) {
                throw new SemanticError("Tipos incompatíveis, " + columnDef.getDataType() + " e " + rightColumn.getDataType());
            }
        } else {
            if (!whereCondition.dataType.getDataTypes().contains(columnDef.getDataType())) {
                throw new SemanticError("Tipos incompatíveis, " + columnDef.getDataType() + " e " + whereCondition.dataType);
            }
        }
    }

}
