package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Field;

import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 9/7/16.
 */
public class SQLQueryInfo {

    /**
     * The model being requested
     */
    private Model model;

    private Map<Field, List<SQLQueryField>> fields;

    /**
     * The list of joined tables involved
     */
    private List<SQLJoinedTable> tables;

    /**
     * The SQL instruction
     */
    private String sql;

    /**
     * Any list of named parameters to be used in SQL query execution
     */
    private Map<String, Object> parameters;

    /**
     * The information about the custom table used in the join
     */
    private SQLJoinedTable customTable;

    public SQLQueryInfo(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public List<SQLJoinedTable> getTables() {
        return tables;
    }

    public void setTables(List<SQLJoinedTable> tables) {
        this.tables = tables;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SQLJoinedTable getCustomTable() {
        return customTable;
    }

    public void setCustomTable(SQLJoinedTable customTable) {
        this.customTable = customTable;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<Field, List<SQLQueryField>> getFields() {
        return fields;
    }

    public void setFields(Map<Field, List<SQLQueryField>> fields) {
        this.fields = fields;
    }
}
