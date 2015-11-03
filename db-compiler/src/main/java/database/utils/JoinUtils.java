package database.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import database.metadata.ColumnDef;
import database.metadata.interfaces.ITableDef;

public class JoinUtils {

    public static void runRegistryComparators(IRegistry registry, List<AbstractValueComparator> comparators, boolean[] results) {

        for (AbstractValueComparator comparator : comparators) {
            boolean result = comparator.isValid(registry.columnValue.get(comparator.columnLeft));
            results[comparator.order] = result;
        }
    }
    
    private static void runJoinComparators(IRegistry registry, HashMap<ITableDef, List<AbstractValueComparator>> joinConditions, boolean[] results) {
    	for(Entry<ITableDef, List<AbstractValueComparator>> entry : joinConditions.entrySet()) {
    		for (AbstractValueComparator comparator : entry.getValue()) {
    			if (registry.columnValue.containsKey(comparator.columnLeft) && registry.columnValue.containsKey(comparator.columnRight)) {
    				boolean result = comparator.isValid(registry.columnValue.get(comparator.columnLeft), registry.columnValue.get(comparator.columnRight));
    				results[comparator.order] = result;
    			}
    		}
    	}
    }

    public static class IRegistry {

        Map<ColumnDef, Object> columnValue = new HashMap<ColumnDef, Object>();
        
        public void mergeWith(IRegistry registryToMerge) {
        	this.columnValue.putAll(registryToMerge.columnValue);
        }

    }

    public static class TableJoinRegistry implements Comparable<TableJoinRegistry> {

        List<IRegistry> registrys = new LinkedList<JoinUtils.IRegistry>();
        ITableDef tableDef;
        List<AbstractValueComparator> tableComparators = new ArrayList<>();
        HashMap<ITableDef, List<AbstractValueComparator>> joinConditions = new HashMap<>();

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

        Arrays.sort(registries);
        Queue<TableJoinRegistry> tableQueue = new LinkedList<JoinUtils.TableJoinRegistry>(Arrays.asList(registries));
        result.addAll(checkTable(logicalComparators, tableQueue.poll(), tableQueue, comparatorsResult, new IRegistry()));
        
        return result;
    }
    
    private static List<IRegistry> checkTable(ArrayList<AbstractBooleanComparator> logicalComparators, TableJoinRegistry table, Queue<TableJoinRegistry> tableQueue, boolean[] comparatorsResult, IRegistry actualMergedRegistry) {
    	for (IRegistry actualRegistry : table.registrys) {
    		actualMergedRegistry.mergeWith(actualRegistry);
    		
            runRegistryComparators(actualRegistry, table.tableComparators, comparatorsResult);
            runJoinComparators(actualMergedRegistry, table.joinConditions, comparatorsResult);
            
            TableJoinRegistry nextTable = tableQueue.poll();
            if (tableQueue.isEmpty()) {
            	return checkLastTable(logicalComparators, nextTable, comparatorsResult, actualMergedRegistry);
            } else {
            	return checkTable(logicalComparators, nextTable, tableQueue, comparatorsResult, actualMergedRegistry);
            }
        }
    	return null;
    }

    private static List<IRegistry> checkLastTable(ArrayList<AbstractBooleanComparator> logicalComparators, TableJoinRegistry table, boolean[] comparatorsResult, IRegistry actualMergedRegistry) {
    	List<IRegistry> validRegistrys = new LinkedList<JoinUtils.IRegistry>();
    	for (IRegistry actualRegistry : table.registrys) {
    		boolean[] actualComparatorsResult = Arrays.copyOf(comparatorsResult, comparatorsResult.length);
            runRegistryComparators(actualRegistry, table.tableComparators, actualComparatorsResult);

            runJoinComparators(actualMergedRegistry, table.joinConditions, actualComparatorsResult);
            
            if (isTrue(logicalComparators, actualComparatorsResult)) {
            	IRegistry newValidRegistry = new IRegistry();
            	newValidRegistry.mergeWith(actualRegistry);
            	newValidRegistry.mergeWith(actualMergedRegistry);
            	
            	validRegistrys.add(actualMergedRegistry);
            }
        }
    	return validRegistrys;
    }
    
    private static boolean isTrue(ArrayList<AbstractBooleanComparator> conditions, boolean[] values) {
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
