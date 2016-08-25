package org.msh.etbm.commons.sqlquery;

/**
 * Created by rmemoria on 16/8/16.
 */
public class SQLTable {

    private String tableName;
    private String tableAlias;
    private String on;
    private boolean leftJoin;
    private boolean root;
    /**
     * The name of the join to be used alternatively as reference in queries
     */
    private String joinName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public boolean isLeftJoin() {
        return leftJoin;
    }

    public void setLeftJoin(boolean leftJoin) {
        this.leftJoin = leftJoin;
    }

    public boolean isRoot() {
        return root;
    }

    /**
     * Create the table representing the root table in the join list
     * @param tableName
     * @return
     */
    public static final SQLTable createRoot(String tableName) {
        SQLTable tbl = new SQLTable();
        tbl.setTableName(tableName);
        tbl.setJoinName(tableName);
        tbl.root = true;
        return tbl;
    }

    public String getJoinName() {
        return joinName;
    }

    public void setJoinName(String joinName) {
        this.joinName = joinName;
    }
}
