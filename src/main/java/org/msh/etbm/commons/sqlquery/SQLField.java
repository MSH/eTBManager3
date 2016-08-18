package org.msh.etbm.commons.sqlquery;

import java.util.Optional;

/**
 * Store information about a field of a SELECT statement
 * Created by rmemoria on 16/8/16.
 */
public class SQLField {

    /**
     * The field name (or an expression that is not an aggregation involving other fields)
     */
    private String fieldName;

    /**
     * The field alias used in the select operation
     */
    private String fieldAlias;

    /**
     * If true, it indicates it is not necessarily a field, but an aggregate expression like
     * COUNT or SUM. It also indicates that query shall contain a GROUP BY clause
     */
    private boolean aggregation;

    /**
     * An optional join table related to the field
     */
    private SQLTable table;


    public SQLField(String fieldName, String fieldAlias, boolean aggregation, SQLTable table) {
        this.fieldName = fieldName;
        this.fieldAlias = fieldAlias;
        this.aggregation = aggregation;
        this.table = table;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public boolean isAggregation() {
        return aggregation;
    }

    public SQLTable getTable() {
        return table;
    }

    public void setTable(SQLTable table) {
        this.table = table;
    }

    public void setAggregation(boolean aggregation) {
        this.aggregation = aggregation;
    }
}
