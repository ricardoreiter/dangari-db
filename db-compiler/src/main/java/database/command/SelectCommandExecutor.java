package database.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.manager.DatabaseManager;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;
import database.utils.AbstractBooleanComparator;
import database.utils.AbstractValueComparator;
import database.utils.JoinUtils;
import database.utils.JoinUtils.IRegistry;
import database.utils.JoinUtils.TableJoinRegistry;

public class SelectCommandExecutor implements ICommandExecutor {

	private List<ITableDef> tableList;
	public List<ITableDef> getTableList() {
		return tableList;
	}

	public ArrayList<AbstractBooleanComparator> getWhereConditionsLogicalOperators() {
		return whereConditionsLogicalOperators;
	}

	public Map<ITableDef, List<AbstractValueComparator>> getTableComparators() {
		return tableComparators;
	}

	public Map<ITableDef, HashMap<ITableDef, List<AbstractValueComparator>>> getTableJoinComparators() {
		return tableJoinComparators;
	}

	public List<IColumnDef> getSelectedColumns() {
		return selectedColumns;
	}

	private ArrayList<AbstractBooleanComparator> whereConditionsLogicalOperators;
	private Map<ITableDef, List<AbstractValueComparator>> tableComparators;
	private Map<ITableDef, HashMap<ITableDef, List<AbstractValueComparator>>> tableJoinComparators;
	private List<IColumnDef> selectedColumns;
	
	public SelectCommandExecutor(List<ITableDef> tableList, ArrayList<AbstractBooleanComparator> whereConditionsLogicalOperators, 
			Map<ITableDef, List<AbstractValueComparator>> tableComparators, Map<ITableDef, HashMap<ITableDef, List<AbstractValueComparator>>> tableJoinComparators,
			List<IColumnDef> selectedColumns) {
		this.tableList = tableList;
		this.whereConditionsLogicalOperators = whereConditionsLogicalOperators;
		this.tableComparators = tableComparators;
		this.tableJoinComparators = tableJoinComparators;
		this.selectedColumns = selectedColumns;
	}
	
    @Override
    public CommandResult execute() {
        IDatabaseDef database = DatabaseManager.INSTANCE.getActualDatabase();
        
        TableJoinRegistry[] tablesJoinRegistry = new TableJoinRegistry[tableList.size()];
        int i = 0;
        for (ITableDef tableDef : tableList) {
        	TableJoinRegistry tableJoinRegistry = new TableJoinRegistry();
        	tableJoinRegistry.tableDef = tableDef;
        	tableJoinRegistry.joinConditions = tableJoinComparators.get(tableDef);
        	tableJoinRegistry.tableComparators = tableComparators.get(tableDef);
        	tableJoinRegistry.registrys = database.getRecords(tableDef);
			tablesJoinRegistry[i] = tableJoinRegistry;
        	i++;
        }
        
        CommandResult commandResult = new CommandResult();
        i = 1;
        for (IColumnDef columnDef : selectedColumns) {
        	commandResult.addColumn(columnDef.getName() + " - " + i);
        	i++;
        }
        
        List<IRegistry> result = JoinUtils.joinTables(whereConditionsLogicalOperators, tablesJoinRegistry);
        for (IRegistry registry : result) {
        	i = 1;
        	for (IColumnDef columnDef : selectedColumns) {
            	commandResult.addValue(columnDef.getName() + " - " + i, registry.columnValue.get(columnDef).toString());
            	i++;
            }
        }
        return commandResult;
    }

}
