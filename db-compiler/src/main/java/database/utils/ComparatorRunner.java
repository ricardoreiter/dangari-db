package database.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import database.metadata.ColumnDef;

public class ComparatorRunner {

	public static boolean isRegistryValid(LinkedHashMap<AbstractValueComparator, ColumnDef> validatorsMap, List<AbstractBooleanComparator> logicalComparators) {
		boolean isValid = false;
		
		int i = 0;
		for (Entry<AbstractValueComparator, ColumnDef> entry : validatorsMap.entrySet()) {
			isValid = entry.getKey().isValid(0);
			//todo fazendo ainda
			i++;
		}
		
		return isValid;
	}
	
}
