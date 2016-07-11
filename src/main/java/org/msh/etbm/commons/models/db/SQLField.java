package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.models.data.fields.Field;

/**
 * Created by rmemoria on 9/7/16.
 */
public class SQLField {

    private String fieldName;
    private SQLJoinedTable table;
    private Field field;
    private String fieldNameAlias;

    public SQLField(Field field, String fieldName, SQLJoinedTable table) {
        this.field = field;
        this.fieldName = fieldName;
        this.table = table;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SQLJoinedTable getTable() {
        return table;
    }

    public void setTable(SQLJoinedTable table) {
        this.table = table;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getFieldNameAlias() {
        return fieldNameAlias;
    }

    public void setFieldNameAlias(String fieldNameAlias) {
        this.fieldNameAlias = fieldNameAlias;
    }
}
