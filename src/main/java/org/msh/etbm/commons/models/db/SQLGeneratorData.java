package org.msh.etbm.commons.models.db;

import java.util.Map;

/**
 * Created by rmemoria on 7/7/16.
 */
public class SQLGeneratorData {

    /**
     * The SQL instruction
     */
    private String sql;

    /**
     * The query parameters
     */
    private Map<String, Object> params;

    protected SQLGeneratorData(String sql, Map<String, Object> params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
