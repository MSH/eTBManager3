package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.models.data.Model;

import java.util.List;

/**
 * Created by rmemoria on 9/7/16.
 */
public class SQLSelectResult {

    private Model model;
    private List<SQLField> fields;
    private List<SQLJoinedTable> tables;
    private String sql;

    public SQLSelectResult(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public List<SQLField> getFields() {
        return fields;
    }

    public void setFields(List<SQLField> fields) {
        this.fields = fields;
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
}
