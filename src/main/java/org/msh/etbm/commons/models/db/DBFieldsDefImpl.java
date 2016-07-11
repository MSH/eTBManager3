package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.models.data.fields.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link DBFieldsDef}, to define the fields to be returned by the query
 * Created by rmemoria on 9/7/16.
 */
public class DBFieldsDefImpl implements DBFieldsDef {

    private Field field;
    private SQLSelectResult selectResult;
    private SQLJoinedTable table;
    private String rootTable;

    public DBFieldsDefImpl(Field field, SQLSelectResult res, String rootTable) {
        this.field = field;
        this.selectResult = res;
        this.rootTable = rootTable;
    }

    public DBFieldsDefImpl(SQLJoinedTable table, Field field, SQLSelectResult res, String rootTable) {
        this.table = table;
        this.field = field;
        this.selectResult = res;
        this.rootTable = rootTable;
    }

    @Override
    public DBFieldsDef add(String fieldName) {
        if (selectResult.getFields() == null) {
            selectResult.setFields(new ArrayList<>());
        }

        SQLField item = new SQLField(field, fieldName, table);
        selectResult.getFields().add(item);

        return this;
    }

    @Override
    public DBFieldsDef join(String tableName, String on) {
        SQLJoinedTable tbl = new SQLJoinedTable(tableName, on);
        tbl.setTableAlias(createTableAlias());

        if (selectResult.getTables() == null) {
            selectResult.setTables(new ArrayList<>());
        }

        selectResult.getTables().add(tbl);

        return new DBFieldsDefImpl(tbl, field, selectResult, rootTable);
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
