package database.command.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import database.command.ICommandExecutor;
import database.command.SelectCommandExecutor;
import database.gals.SemanticError;
import database.gals.Token;
import database.manager.DatabaseManager;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.utils.AbstractBooleanComparator;
import database.utils.AbstractValueComparator;
import database.utils.AndBooleanComparator;
import database.utils.EqualsValueComparator;
import database.utils.GreaterOrEqualsValueComparator;
import database.utils.GreaterValueComparator;
import database.utils.LessOrEqualsValueComparator;
import database.utils.LessValueComparator;
import database.utils.NotEqualsValueComparator;
import database.utils.OrBooleanComparator;

public class SelectCommandCompiler implements ICommandCompiler {

    private LinkedList<LinkedList<String>> selectedFields = new LinkedList<>(); // Campos selecionados (ex: codigoArea ou tabela.codigoArea)
    private List<String> selectedTables = new LinkedList<>(); // Tabelas selecionadas
    private LinkedList<WhereCondition> whereConditions = new LinkedList<>();
    private List<String> whereLogicalConditions = new LinkedList<>();

    private boolean isCompilingWhere = false;
    private boolean newWhere = true;

    private int whereOrder = 0;

    private class WhereCondition {

        public LinkedList<String> right;
        public LinkedList<String> left;
        public String relationalOperation;
        public CompilerDataType dataType;
        public int order;
    }

    @Override
    public void accept(int action, Token token) {
        switch (action) {
            case 10:
                whereConditions.getLast().dataType = CompilerDataType.INTEGER;
                whereConditions.getLast().right = new LinkedList<>();
                whereConditions.getLast().right.add(token.getLexeme());
                break;
            case 11:
                whereConditions.getLast().dataType = CompilerDataType.LITERAL;
                whereConditions.getLast().right = new LinkedList<>();
                whereConditions.getLast().right.add(token.getLexeme().replace("\"", ""));
                break;
            case 12:
                whereConditions.getLast().dataType = CompilerDataType.NULL;
                whereConditions.getLast().right = new LinkedList<>();
                whereConditions.getLast().right.add(token.getLexeme());
                break;
            case 40:
                if (isCompilingWhere) {
                    if (!newWhere && whereConditions.size() > 0 && whereConditions.getLast().relationalOperation != null) {
                        whereConditions.getLast().right = new LinkedList<>();
                        whereConditions.getLast().right.add(token.getLexeme());
                        whereConditions.getLast().dataType = CompilerDataType.FIELD;
                    } else {
                        WhereCondition wc = new WhereCondition();
                        wc.left = new LinkedList<>();
                        wc.left.add(token.getLexeme());
                        wc.order = whereOrder++;
                        whereConditions.add(wc);
                        newWhere = false;
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
                newWhere = true;
                break;
        }
    }

    @Override
    public ICommandExecutor compile() throws SemanticError {
        IDatabaseDef database = DatabaseManager.getInstance().getActualDatabase();

        if (database == null) {
            throw new SemanticError("Nenhum database ativo. Use [SET DATABASE NOME] para definir um database ativo.");
        }

        System.out.println("-- Conferindo campos selecionados --");
        ArrayList<String> checkedFields = new ArrayList<String>(selectedFields.size());
        List<IColumnDef> selectedColumns = new LinkedList<IColumnDef>();
        for (LinkedList<String> field : selectedFields) {
            selectedColumns.addAll(checkSelectedField(database, field, checkedFields, true));
        }

        List<ITableDef> tables = new LinkedList<ITableDef>();
        for (String table : selectedTables) {
            tables.add(checkTable(database, table));
        }

        HashMap<ITableDef, List<AbstractValueComparator>> tableComparators = new HashMap<ITableDef, List<AbstractValueComparator>>();
        Map<ITableDef, HashMap<ITableDef, List<AbstractValueComparator>>> tableJoinComparators = new HashMap<ITableDef, HashMap<ITableDef, List<AbstractValueComparator>>>();
        for (WhereCondition whereCondition : whereConditions) {
            checkWhere(database, whereCondition, tableComparators, tableJoinComparators);
        }

        return new SelectCommandExecutor(tables, getLogicalComparators(), tableComparators, tableJoinComparators, selectedColumns);
    }

    private ArrayList<AbstractBooleanComparator> getLogicalComparators() {
        ArrayList<AbstractBooleanComparator> result = new ArrayList<AbstractBooleanComparator>();
        for (String comparator : whereLogicalConditions) {
            switch (comparator.toLowerCase()) {
                case "and":
                    result.add(new AndBooleanComparator());
                    break;
                default:
                    result.add(new OrBooleanComparator());
                    break;
            }
        }
        return result;
    }

    private List<IColumnDef> checkSelectedField(IDatabaseDef database, LinkedList<String> tableAndField, ArrayList<String> checkedFields, boolean allowAllFields) throws SemanticError {
        IColumnDef columnDef = null;
        String tableField;
        LinkedList<IColumnDef> resultList = new LinkedList<IColumnDef>();
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
                resultList.addAll(tableDef.getColumns());
            } else {
                columnDef = tableDef.getColumnDef(field);
                if (columnDef == null) {
                    throw new SemanticError("Tabela " + table + " não possuí nenhuma coluna " + field);
                }
                resultList.add(columnDef);
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
            resultList.add(columnDef);
        }

        if (checkedFields.contains(tableField)) {
            throw new SemanticError("Campo " + tableField + " já declarado");
        } else {
            checkedFields.add(tableField);
        }
        return resultList;
    }

    private ITableDef checkTable(IDatabaseDef database, String tableName) throws SemanticError {
        ITableDef tableDef = database.getTableDef(tableName);
        if (tableDef == null) {
            throw new SemanticError("Tabela " + tableName + " não existe");
        }
        return tableDef;
    }

    private ITableDef getWhereTableDef(IDatabaseDef database, LinkedList<String> tableAndField) throws SemanticError {
        if (tableAndField.size() > 1) { // Se está estruturado em tabela.coluna
            String table = tableAndField.getFirst();
            String field = tableAndField.getLast();

            if (!selectedTables.contains(table)) {
                throw new SemanticError("Tabela " + table + " não está presente na cláusula FROM");
            }
            ITableDef tableDef = checkTable(database, table);

            if (field.equals("*")) {
                throw new SemanticError("Não é possível utilizar a seleção de todos os campos '*', é necessário especificar um campo");
            }
            return tableDef;
        } else {
            if (selectedTables.size() > 1) {
                throw new SemanticError("Você deve informar a tabela para a coluna " + tableAndField.getFirst());
            }
            String tableField = tableAndField.getFirst();
            ITableDef tableDef = checkTable(database, selectedTables.get(0));
            IColumnDef columnDef = tableDef.getColumnDef(tableField);
            if (columnDef == null) {
                throw new SemanticError("Tabela " + selectedTables.get(0) + " não possuí nenhuma coluna " + tableField);
            }
            return tableDef;
        }
    }

    private AbstractValueComparator getComparator(String operation, IColumnDef columnLeft, IColumnDef columnRight, Object constantValue) {
        switch (operation) {
            case "=":
                return new EqualsValueComparator(constantValue, columnLeft, columnRight);
            case "<>":
                return new NotEqualsValueComparator(constantValue, columnLeft, columnRight);
            case ">":
                return new GreaterValueComparator(constantValue, columnLeft, columnRight);
            case "<":
                return new LessValueComparator(constantValue, columnLeft, columnRight);
            case ">=":
                return new GreaterOrEqualsValueComparator(constantValue, columnLeft, columnRight);
            case "<=":
                return new LessOrEqualsValueComparator(constantValue, columnLeft, columnRight);
            default:
                return null;
        }
    }

    private void checkWhere(IDatabaseDef database, WhereCondition whereCondition, HashMap<ITableDef, List<AbstractValueComparator>> tableComparators, Map<ITableDef, HashMap<ITableDef, List<AbstractValueComparator>>> tableJoinComparators) throws SemanticError {
        IColumnDef leftColumn = checkSelectedField(database, whereCondition.left, new ArrayList<String>(), false).get(0);
        ITableDef leftTable = getWhereTableDef(database, whereCondition.left);

        if (whereCondition.dataType == CompilerDataType.FIELD) {
            IColumnDef rightColumn = checkSelectedField(database, whereCondition.right, new ArrayList<String>(), false).get(0);
            if (leftColumn.getDataType() != rightColumn.getDataType()) {
                throw new SemanticError("Tipos incompatíveis, " + leftColumn.getDataType() + " e " + rightColumn.getDataType());
            }

            ITableDef rightTable = getWhereTableDef(database, whereCondition.right);

            if (leftTable == rightTable) { // se as tabelas dos dois campos são iguais, então não é um joinComparator
                addTableComparator(database, whereCondition, tableComparators, leftColumn, leftTable, rightColumn);
            } else {
            	// Adiciona o joinComparator na tabela left
                addJoinComparators(database, whereCondition, tableComparators, tableJoinComparators, leftColumn, leftTable, rightColumn, rightTable);
                
                // Adiciona o mesmo joinComparator na tabela right (O algoritmo de join não sabe em qual das iterações das tabelas q ele vai executar, 
                // ele só executa o joinComparator quanto já tem os registros de ambas as tabelas
                addJoinComparators(database, whereCondition, tableComparators, tableJoinComparators, rightColumn, rightTable, leftColumn, leftTable);
            }
        } else {
            if (!whereCondition.dataType.getDataTypes().contains(leftColumn.getDataType())) {
                throw new SemanticError("Tipos incompatíveis, " + leftColumn.getDataType() + " e " + whereCondition.dataType);
            }

            addTableComparator(database, whereCondition, tableComparators, leftColumn, leftTable, null);
        }
    }

    private void addJoinComparators(IDatabaseDef database, WhereCondition whereCondition, HashMap<ITableDef, List<AbstractValueComparator>> tableComparators, Map<ITableDef, HashMap<ITableDef, List<AbstractValueComparator>>> tableJoinComparators, IColumnDef columnDef, ITableDef leftTable, IColumnDef rightColumn, ITableDef rightTable) {
        HashMap<ITableDef, List<AbstractValueComparator>> tableLeftJoinComparators;
        if (tableJoinComparators.containsKey(leftTable)) {
            tableLeftJoinComparators = tableJoinComparators.get(leftTable);
        } else {
            tableLeftJoinComparators = new HashMap<ITableDef, List<AbstractValueComparator>>();
            tableJoinComparators.put(leftTable, tableLeftJoinComparators);
        }

        List<AbstractValueComparator> comparatorsList;
        if (tableLeftJoinComparators.containsKey(rightTable)) {
            comparatorsList = tableLeftJoinComparators.get(rightTable);
        } else {
            comparatorsList = new LinkedList<AbstractValueComparator>();
            tableLeftJoinComparators.put(rightTable, comparatorsList);
        }

        AbstractValueComparator comparator = getComparator(whereCondition.relationalOperation, columnDef, rightColumn, null);
        comparator.setOrder(whereCondition.order);
        comparatorsList.add(comparator);
    }

    private void addTableComparator(IDatabaseDef database, WhereCondition whereCondition, HashMap<ITableDef, List<AbstractValueComparator>> tableComparators, IColumnDef columnDef, ITableDef leftTable, IColumnDef rightColumn) throws SemanticError {
        List<AbstractValueComparator> list;
        if (tableComparators.containsKey(leftTable)) {
            list = tableComparators.get(leftTable);
        } else {
            list = new LinkedList<AbstractValueComparator>();
            tableComparators.put(leftTable, list);
        }

        Object constantValue = null;
        if (rightColumn == null) {
            if (whereCondition.dataType == CompilerDataType.INTEGER) {
                constantValue = new Integer(whereCondition.right.getFirst());
            } else {
                constantValue = whereCondition.right.getFirst();
            }
        }

        AbstractValueComparator comparator = getComparator(whereCondition.relationalOperation, columnDef, rightColumn, constantValue);
        comparator.setOrder(whereCondition.order);
        list.add(comparator);
    }

}
