package database.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Result {

	private Map<Column, List<String>> resultMap;

	public Column[] getCols() {
		final Set<Column> keySet = getResultMap().keySet();
		if (keySet.isEmpty()) {
			return null;
		}
		final Column[] retorno = new Column[keySet.size()];
		final Iterator<Column> iterator = keySet.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			retorno[i++] = iterator.next();
		}
		return retorno;
	}

	public Map<Column, List<String>> getResultMap() {
		return resultMap;
	}

	public void addLine(Column col, List<String> dados) {
		if (resultMap == null) {
			resultMap = new HashMap<Column, List<String>>();
		}
		getResultMap().put(col, dados);
	}

}
