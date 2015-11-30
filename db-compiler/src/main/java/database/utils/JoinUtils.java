package database.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import database.metadata.Index;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;

public class JoinUtils {

    private static void runRegistryComparators(IRegistry registry, List<AbstractValueComparator> comparators, boolean[] results) {

        if (comparators != null) {
            for (AbstractValueComparator comparator : comparators) {
                boolean result = false;
                // Em algumas situações, ele pode comparar dois campos de uma mesma tabela, então não é considerado um joinComparator
                if (comparator.getColumnRight() != null) {
                    result = comparator.isValid(registry.columnValue.get(comparator.getColumnLeft()), registry.columnValue.get(comparator.getColumnRight()));
                } else {
                    result = comparator.isValid(registry.columnValue.get(comparator.getColumnLeft()));
                }

                results[comparator.getOrder()] = result;
            }
        }
    }

    private static void runJoinComparators(IRegistry registry, HashMap<ITableDef, List<AbstractValueComparator>> joinConditions, boolean[] results) {
        if (joinConditions != null) {
            for (Entry<ITableDef, List<AbstractValueComparator>> entry : joinConditions.entrySet()) {
                for (AbstractValueComparator comparator : entry.getValue()) {
                    if (registry.columnValue.containsKey(comparator.getColumnLeft()) && registry.columnValue.containsKey(comparator.getColumnRight())) {
                        boolean result = comparator.isValid(registry.columnValue.get(comparator.getColumnLeft()), registry.columnValue.get(comparator.getColumnRight()));
                        results[comparator.getOrder()] = result;
                    }
                }
            }
        }
    }

    public static class IRegistry {

        public Map<IColumnDef, Object> columnValue = new HashMap<IColumnDef, Object>();

        public void mergeWith(IRegistry registryToMerge) {
            this.columnValue.putAll(registryToMerge.columnValue);
        }

    }

    public static class TableJoinRegistry implements Comparable<TableJoinRegistry> {

        public List<IRegistry> registrys;
        public ITableDef tableDef;
        public List<AbstractValueComparator> tableComparators = new ArrayList<>();
        public HashMap<ITableDef, List<AbstractValueComparator>> joinConditions = new HashMap<>();

        public TableJoinRegistry() {
            this(10);
        }

        public TableJoinRegistry(int recordCount) {
            registrys = new ArrayList<JoinUtils.IRegistry>(recordCount);
        }

        @Override
        public int compareTo(TableJoinRegistry o) {
            if (o == this || o.registrys.size() == this.registrys.size()) {
                return 0;
            }

            if (o.registrys.size() > this.registrys.size()) {
                return -1;
            }
            return 1;
        }

    }

    public static List<IRegistry> joinTables(ArrayList<AbstractBooleanComparator> logicalComparators, TableJoinRegistry... registries) {
        List<IRegistry> result = new LinkedList<JoinUtils.IRegistry>();
        boolean[] comparatorsResult = new boolean[logicalComparators.size() + 1];
        Arrays.fill(comparatorsResult, true);

        Arrays.sort(registries);
        result.addAll(checkTable(logicalComparators, 0, registries, comparatorsResult, new IRegistry()));

        return result;
    }

    private static List<IRegistry> checkTable(ArrayList<AbstractBooleanComparator> logicalComparators, int currentTable, TableJoinRegistry[] tableList, boolean[] comparatorsResult, IRegistry actualMergedRegistry) {
        if (currentTable == tableList.length - 1) {
            return checkLastTable(logicalComparators, tableList[currentTable], comparatorsResult, actualMergedRegistry);
        }

        Set<Integer> validIndexesToIterate = getValidIndexes(actualMergedRegistry, tableList[currentTable]);

        List<IRegistry> validRegistrys = new LinkedList<JoinUtils.IRegistry>();
        List<IRegistry> tableRegistrys = tableList[currentTable].registrys;
        for (Integer actualIndex : validIndexesToIterate) {
            IRegistry actualRegistry = tableRegistrys.get(actualIndex);
            IRegistry newRegistry = new IRegistry();
            newRegistry.mergeWith(actualRegistry);
            newRegistry.mergeWith(actualMergedRegistry);
            boolean[] actualComparatorsResult = Arrays.copyOf(comparatorsResult, comparatorsResult.length);

            runRegistryComparators(actualRegistry, tableList[currentTable].tableComparators, actualComparatorsResult);
            runJoinComparators(newRegistry, tableList[currentTable].joinConditions, actualComparatorsResult);

            if (!isTrue(logicalComparators, actualComparatorsResult)) {
                continue;
            }

            validRegistrys.addAll(checkTable(logicalComparators, currentTable + 1, tableList, actualComparatorsResult, newRegistry));
        }
        return validRegistrys;
    }

    /**
     * @param actualMergedRegistry
     * @param tableJoinRegistry
     * @return
     */
    private static Set<Integer> getValidIndexes(IRegistry actualMergedRegistry, TableJoinRegistry tableJoinRegistry) {
        Set<Integer> result = new LinkedHashSet<>();
        boolean addAll = true;
        if (tableJoinRegistry.tableComparators != null) {
            for (AbstractValueComparator actualComparator : tableJoinRegistry.tableComparators) {
                Index index = tableJoinRegistry.tableDef.getIndex(actualComparator.getColumnLeft());
                if (index == null) {
                    addAll = true;
                    break;
                }
                addAll = false;
                result.addAll(actualComparator.getIndexes(index, actualComparator.getConstantValue()));
            }
        }

        if (tableJoinRegistry.joinConditions != null) {
            outer: for (Entry<ITableDef, List<AbstractValueComparator>> entry : tableJoinRegistry.joinConditions.entrySet()) {
                for (AbstractValueComparator comparator : entry.getValue()) {

                    if (actualMergedRegistry.columnValue.containsKey(comparator.getColumnLeft())) {
                        Index index = tableJoinRegistry.tableDef.getIndex(comparator.getColumnRight());
                        if (index == null) {
                            addAll = true;
                            break outer;
                        }
                        addAll = false;
                        result.addAll(comparator.getIndexes(index, actualMergedRegistry.columnValue.get(comparator.getColumnLeft())));

                        // Bem escroto mermo
                    } else if (actualMergedRegistry.columnValue.containsKey(comparator.getColumnRight())) {
                        Index index = tableJoinRegistry.tableDef.getIndex(comparator.getColumnLeft());
                        if (index == null) {
                            addAll = true;
                            break outer;
                        }
                        addAll = false;
                        result.addAll(comparator.getIndexes(index, actualMergedRegistry.columnValue.get(comparator.getColumnRight())));
                    }

                }
            }
        }

        if (addAll) {
            for (int i = 0; i < tableJoinRegistry.tableDef.getRowsCount(); i++) {
                result.add(i);
            }
        }

        return result;
    }

    private static List<IRegistry> checkLastTable(ArrayList<AbstractBooleanComparator> logicalComparators, TableJoinRegistry table, boolean[] comparatorsResult, IRegistry actualMergedRegistry) {
        List<IRegistry> validRegistrys = new LinkedList<JoinUtils.IRegistry>();
        for (IRegistry actualRegistry : table.registrys) {
            IRegistry newRegistry = new IRegistry();
            newRegistry.mergeWith(actualRegistry);
            newRegistry.mergeWith(actualMergedRegistry);
            boolean[] actualComparatorsResult = Arrays.copyOf(comparatorsResult, comparatorsResult.length);
            runRegistryComparators(actualRegistry, table.tableComparators, actualComparatorsResult);

            runJoinComparators(newRegistry, table.joinConditions, actualComparatorsResult);

            if (isTrue(logicalComparators, actualComparatorsResult)) {
                validRegistrys.add(newRegistry);
            }
        }
        return validRegistrys;
    }

    private static boolean isTrue(ArrayList<AbstractBooleanComparator> conditions, boolean[] values) {
        if (conditions.isEmpty()) {
            return values[0];
        }
        if (conditions.size() != values.length - 1) {
            throw new IllegalArgumentException("É necessário passra uma lista de comparadores que seja do tamanho da lista de valores - 1");
        }
        boolean result = values[0];
        for (int i = 1; i < values.length; i++) {
            result = conditions.get(i - 1).isValid(result, values[i]);
        }
        return result;
    }
}
