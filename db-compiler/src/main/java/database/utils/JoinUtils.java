package database.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import database.metadata.ColumnDef;
import database.metadata.interfaces.ITableDef;

public class JoinUtils {

    public static void runRegistryComparators(IRegistry registry, List<AbstractValueComparator> comparators, boolean[] results) {

        for (AbstractValueComparator comparator : comparators) {
            boolean result = comparator.isValid(registry.columnValue.get(comparator.columnLeft));
            results[comparator.order] = result;
        }
    }

    public class IRegistry {

        Map<ColumnDef, Object> columnValue = new HashMap<ColumnDef, Object>();

    }

    public class TableJoinRegistry implements Comparable<TableJoinRegistry> {

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

    public static List<IRegistry> joinTables(List<AbstractBooleanComparator> logicalComparators, TableJoinRegistry... registries) {
        List<IRegistry> result = new LinkedList<JoinUtils.IRegistry>();
        boolean[] comparatorsResult = new boolean[logicalComparators.size() + 1];

        Arrays.sort(registries);
        List<TableJoinRegistry> tables = Arrays.asList(registries);
        for (int i = 0; i < tables.get(0).registrys.size(); i++) {
            runRegistryComparators(tables.get(0).registrys.get(i), tables.get(0).tableComparators, comparatorsResult);

            // vai para a próxima tabela e continua
        }

        return result;
    }

    public boolean isTrue(ArrayList<AbstractBooleanComparator> conditions, boolean[] values) {
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
