package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.models.data.fields.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link DBFieldsDef}, to define the fields to be returned by the query.
 * Created by rmemoria on 9/7/16.
 */
public class DBFieldsDefImpl implements DBFieldsDef {

    private DBFieldsDefImpl parent;
    private Field field;
    private SQLQueryInfo selectResult;
    private SQLJoinedTable table;
    private String rootTable;

    /**
     * Create a root schema
     * @param field
     * @param res
     * @param rootTable
     */
    public DBFieldsDefImpl(Field field, SQLQueryInfo res, String rootTable) {
        this.field = field;
        this.selectResult = res;
        this.rootTable = rootTable;
    }

    public DBFieldsDefImpl(DBFieldsDefImpl parent, SQLJoinedTable table, Field field, SQLQueryInfo res, String rootTable) {
        this.parent = parent;
        this.table = table;
        this.field = field;
        this.selectResult = res;
        this.rootTable = rootTable;
    }

    @Override
    public DBFieldsDef add(String fieldName) {
        if (selectResult.getFields() == null) {
            selectResult.setFields(new HashMap<>());
        }

        SQLQueryField item = new SQLQueryField(field, fieldName, table);

        Map<Field, List<SQLQueryField>> fields = selectResult.getFields();
        List<SQLQueryField> lst = fields.get(field);
        if (lst == null) {
            lst = new ArrayList<>();
            fields.put(field, lst);
        }
        lst.add(item);

        return this;
    }

    @Override
    public DBFieldsDef join(String tableName, String on) {
        return addJoin(tableName, on, false);
    }

    @Override
    public DBFieldsDef leftJoin(String tableName, String on) {
        return addJoin(tableName, on, true);
    }

    /**
     * Add a join (left or inner) table to the query
     * @param tableName
     * @param on the SQL ON expression of the JOIN operation
     * @param leftJoin
     * @return
     */
    protected DBFieldsDef addJoin(String tableName, String on, boolean leftJoin) {
        String tblAlias = createTableAlias();
        String parsedOn = parseOnExpression(on, tblAlias);

        SQLJoinedTable tbl = new SQLJoinedTable(tableName, parsedOn, leftJoin);
        tbl.setTableAlias(tblAlias);

        if (selectResult.getTables() == null) {
            selectResult.setTables(new ArrayList<>());
        }

        selectResult.getTables().add(tbl);

        return new DBFieldsDefImpl(this, tbl, field, selectResult, rootTable);
    }

    /**
     * Parse the ON expression used in a join operation
     * @param on
     * @return
     */
    private String parseOnExpression(String on, String sthis) {
        return on.replace("$this", sthis)
                .replace("$root", getRootTable())
                .replace("$parent", table != null ? table.getTableAlias() : getRootTable());
    }

    @Override
    public String getRootTable() {
        return rootTable;
    }


    protected String createTableAlias() {
        List<SQLJoinedTable> tables = selectResult.getTables();
        int count = tables == null ? 1 : tables.size() + 1;

        int index = count / 27;
        int letter = count % 27;

        String alias = (char)(97 + letter) + Integer.toString(index);
        return alias;
    }


    public Field getField() {
        return field;
    }
}
