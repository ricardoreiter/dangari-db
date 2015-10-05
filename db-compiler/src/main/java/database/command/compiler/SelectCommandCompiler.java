package database.command.compiler;

import java.util.LinkedList;
import java.util.List;

import database.command.ICommandExecutor;
import database.command.SelectCommandExecutor;
import database.gals.Token;

public class SelectCommandCompiler implements ICommandCompiler {

    private LinkedList<StringBuilder> selectedFields = new LinkedList<>(); // Campos selecionados (ex: codigoArea ou tabela.codigoArea)
    private List<String> selectedTables = new LinkedList<>(); // Tabelas selecionadas
    private LinkedList<WhereCondition> whereConditions = new LinkedList<>();
    private List<String> whereLogicalConditions = new LinkedList<>();

    private boolean isCompilingWhere = false;

    private class WhereCondition {

        public String right;
        public String left;
        public String relationalOperation;
    }

    @Override
    public void accept(int action, Token token) {
        System.out.println("SELECT_COMMAND_COMPILER - Ação #" + action + ", Token: " + token);
        switch (action) {
            case 10:
            case 11:
            case 12:
                whereConditions.getLast().right = token.getLexeme();
                break;
            case 40:
                if (isCompilingWhere) {
                    WhereCondition wc = new WhereCondition();
                    wc.left = token.getLexeme();
                    whereConditions.add(wc);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(token.getLexeme());
                    selectedFields.add(sb);
                }
                break;
            case 41:
            case 42:
                if (isCompilingWhere) {
                    whereConditions.getLast().left += "." + token.getLexeme();
                } else {
                    selectedFields.getLast().append(".").append(token.getLexeme());
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
    public ICommandExecutor compile() {
        System.out.println("-- Campos selecionados --");
        for (StringBuilder field : selectedFields) {
            System.out.println(field);
        }

        System.out.println();
        System.out.println("-- Tabelas selecionadas --");
        for (String table : selectedTables) {
            System.out.println(table);
        }

        System.out.println();
        System.out.println("-- Cláusula where --");
        for (int i = 0; i < whereConditions.size(); i++) {
            System.out.println(whereConditions.get(i));
            if (whereLogicalConditions.size() > 0 && whereLogicalConditions.size() > i) {
                System.out.println(whereLogicalConditions.get(0));
            }
        }

        return new SelectCommandExecutor();
    }

}
