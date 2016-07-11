package org.msh.etbm.commons.models.db;

/**
 * Created by rmemoria on 9/7/16.
 */
public class SQLJoinedTable {
    private String tableName;
    private String on;
    private String tableAlias;

    public SQLJoinedTable(String tableName, String on) {
        this.tableName = tableName;
        this.on = on;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }
}
