package org.msh.etbm.commons.models.db;

/**
 * Store information about a joined table used when constructing the SQL Select query in
 * the class {@link SQLQuerySelectionBuilder}
 *
 * Created by rmemoria on 9/7/16.
 */
public class SQLJoinedTable {
    /**
     * The name of the table to be included in the join operation
     */
    private String tableName;

    /**
     * The criteria used in the join operation, user in the ON clause of the JOIN
     */
    private String on;

    /**
     * The table alias defined for this table
     */
    private String tableAlias;

    /**
     * True if it is a left join operation, or false for a simple join operation
     */
    private boolean leftJoin;

    public SQLJoinedTable(String tableName, String on, boolean leftJoin) {
        this.tableName = tableName;
        this.on = on;
        this.leftJoin = leftJoin;
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

    public boolean isLeftJoin() {
        return leftJoin;
    }

    public void setLeftJoin(boolean leftJoin) {
        this.leftJoin = leftJoin;
    }
}
