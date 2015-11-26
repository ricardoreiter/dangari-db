package database.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.ITableDef;
import database.utils.Fn;

public class TableDef implements ITableDef {

    private String name;
    private int rowsCount;
    private Map<String, IColumnDef> columns = new LinkedHashMap<>();
    private Map<IColumnDef, Index> columnsIndex = new LinkedHashMap<>();

    public TableDef(String name) {
        setName(name);
    }

    public TableDef(String name, IColumnDef... columnsDef) {
        this(name);
        this.rowsCount = 0;
        this.mapColumns(columnsDef);
    }

    public TableDef(String name, int rowsCount, IColumnDef... columnsDef) {
        this(name);
        this.rowsCount = rowsCount;
        this.mapColumns(columnsDef);
    }

    private void mapColumns(IColumnDef[] defs) {
        columns = Fn.map(c -> c.getName(), defs);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        checkName(name);
        this.name = name;
    }

    @Override
    public int getColumnsCount() {
        return columns.size();
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }

    @Override
    public void incrementRowsCount() {
        rowsCount++;
    }

    @Override
    public void decrementRowsCount() {
        rowsCount--;
    }

    @Override
    public IColumnDef getColumnDef(String column) {
        return columns.get(column);
    }

    @Override
    public void addColumnDef(IColumnDef column) {
        checkColumn(column);
        String key = column.getName();
        columns.put(key, column);
    }

    @Override
    public List<IColumnDef> getColumns() {
        return new ArrayList<>(columns.values());
    }

    private void checkName(String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("O nome da tabela não pode ser nulo ou vazio");
        }
    }

    private void checkColumn(IColumnDef column) {
        if (column == null) {
            throw new RuntimeException("Não é possível adicionar uma coluna nula");
        }

        String key = column.getName();
        if (columns.containsKey(key)) {
            throw new RuntimeException(String.format("Já existe uma coluna com o nome %s", column));
        }
    }

    @Override
    public void deleteColumnDef(String column) {
        columns.remove(column);
    }

    @Override
    public Index getIndex(IColumnDef columnDef) {
        return columnsIndex.get(columnDef);
    }

    @Override
    public void addIndex(IColumnDef columnDef, Index index) {
        columnsIndex.put(columnDef, index);
    }

    @Override
    public void deleteIndex(IColumnDef columnDef) {
        columnsIndex.remove(columnDef);
    }

}
