package org.msh.etbm.commons.indicators.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ricardo Memoria
 *
 */
public class TableJoinImpl implements TableJoin {

    private String tableName;
    private String tableField;
    private String parentField;
    private TableJoinImpl parentJoin;
    private int aliasCounter;	// a unique number used to generate the table alias
    private List<TableJoinImpl> joins;
    private boolean leftJoin;
    private String alias;
    private SqlBuilder builder;
    // means that the property was declared inside an iteration
    private boolean iterationContext;

    /**
     * Default constructor
     * @param tableName the name of the main table in the join
     * @param tableField the name of the main field in the join
     * @param parentJoin the parent table in the join
     * @param parentField the name of the field in the parent table of the join
     */
    public TableJoinImpl(SqlBuilder builder, String tableName, String tableField, TableJoinImpl parentJoin, String parentField) {
        super();
        this.tableName = tableName;
        this.tableField = tableField;
        this.parentField = parentField;
        this.parentJoin = parentJoin;
        this.builder = builder;
        generateAliasCounter();
    }


    /**
     * Remove all joins that were declared during iteration context
     */
    public void removeIterationContextJoins() {
        if (joins != null) {
            int index = 0;
            while (index < joins.size()) {
                TableJoinImpl join = joins.get(index);
                if (join.isIterationContext()) {
                    joins.remove(join);
                } else {
                    join.removeIterationContextJoins();
                    index++;
                }
            }
        }
    }

    @Override
    public TableJoinImpl join(String field, String targetfield) {
        String[] s = targetfield.split("\\.");
        if (s.length != 2) {
            throw new RuntimeException("Invalid argument format. 2o argument must be table.field format");
        }

        return join(field, s[0], s[1]);
    }


    public TableJoinImpl join(String field, String newTable, String newField) {
        // check if join is already there
        if (joins != null) {
            for (TableJoinImpl join: joins) {
                if (join.getTableField().equals(newField) && join.getTableName().equals(newTable) && join.getParentField().equals(field)) {
                    return join;
                }
            }
        }
        TableJoinImpl join = new TableJoinImpl(builder, newTable, newField, this, field);
        join.setIterationContext(builder.isCreatingSql());
        addJoin(join);
        return join;
    }


    /**
     * add a field of this table to be selected by the query
     * @param field
     * @return
     */
    public TableJoinImpl select(String field) {
        builder.select(field, this);
        return this;
    }

    /**
     * Add a left join to the current table
     * @param field is the field in the current table
     * @param jointablefield is the representation of table.field of the new joined table
     * @return instance of {@link TableJoinImpl} class
     */
    public TableJoinImpl leftJoin(String field, String jointablefield) {
        TableJoinImpl tbljoin = join(field, jointablefield);
        tbljoin.setLeftJoin(true);
        return tbljoin;
    }

    /**
     * Add a new join to this table
     * @param join instance of the {@link TableJoinImpl} to be included in this main table join
     */
    protected void addJoin(TableJoinImpl join) {
        if (joins == null) {
            joins = new ArrayList<TableJoinImpl>();
        }

        joins.add(join);
    }

    /**
     * Remove a join from this table
     * @param join instance of the {@link TableJoinImpl} representing the join
     */
    public void removeJoin(TableJoinImpl join) {
        if (joins.contains(join)) {
            joins.remove(join);
        }
    }


    /**
     * Search for a join table with the arguments
     * @param table
     * @param tableFieldName
     * @param parentTable
     * @param parentFieldName
     * @return
     */
    protected TableJoinImpl findJoin(String table, String tableFieldName, String parentTable, String parentFieldName) {
        if (tableName.equals(table)) {
            if (equalString(tableField, tableFieldName) && equalString(parentField, parentFieldName)) {
                return this;
            }
        }

        if (joins != null) {
            for (TableJoinImpl join: joins) {
                TableJoinImpl tj = join.findJoin(table, tableFieldName, parentTable, parentFieldName);
                if (tj != null) {
                    return tj;
                }
            }
        }
        return null;
    }


    /**
     * Remove all joins of the table
     */
    public void removeAllJoins() {
        joins = null;
    }


    /**
     * Find join just by its table name
     * @param tableName
     * @return instance of the {@link TableJoinImpl} class, or null if no table is found
     */
    protected TableJoinImpl findJoin(String tableName) {
        if (this.tableName.equals(tableName)) {
            return this;
        }

        if (joins != null) {
            for (TableJoinImpl join: joins) {
                TableJoinImpl tj = join.findJoin(tableName);
                if (tj != null) {
                    return tj;
                }
            }
        }
        return null;
    }

    /**
     * Check if two strings are equals, testing also if they are null
     * @param val1 String value
     * @param val2 String value to be compared
     * @return true if they are equals, otherwise returns false
     */
    protected boolean equalString(String val1, String val2) {
        // both are null or pointing to the same string?
        if (val1 == val2) {
            return true;
        }

        // one of them are null value
        if ((val1 == null) || (val2 == null)) {
            return false;
        }

        return val1.equals(val2);
    }

    /**
     * Generate a new alias counter for this join
     */
    protected void generateAliasCounter() {
        // search for root table
        TableJoinImpl root = this;
        while (root.getParentJoin() != null) {
            root = root.getParentJoin();
        }

        aliasCounter = root == this ? 1 : root.calcMaxAliasCounter() + 1;
    }


    /**
     * Calculate the biggest alias counter from a table join node
     * @return
     */
    protected int calcMaxAliasCounter() {
        int num = aliasCounter;

        // there are children ?
        if (joins != null) {
            // search for a bigger number in the children
            for (TableJoinImpl join : joins) {
                int n = join.calcMaxAliasCounter();
                if (n > num) {
                    num = n;
                }
            }
        }

        return num;
    }


    /**
     * Create a new table alias based on the alias counter
     * @return
     */
    protected String createTableAlias() {
        int lo = aliasCounter % 26;
        int hi = aliasCounter / 26;

        String res = "" + (char)(97 + lo);
        if (hi > 0) {
            res = (char) hi + res;
        }

        return res;
    }



    /**
     * @return the alias
     */
    @Override
    public String getAlias() {
        if (alias != null) {
            return alias;
        }
        return createTableAlias();
    }


    /** {@inheritDoc}
     */
    @Override
    public String getTableName() {
        return tableName;
    }


    /** {@inheritDoc}
     */
    @Override
    public TableJoinImpl getParentJoin() {
        return parentJoin;
    }


    /**
     * @return the joins
     */
    public List<TableJoinImpl> getJoins() {
        return joins;
    }


    /**
     * @return the tableField
     */
    public String getTableField() {
        return tableField;
    }


    /**
     * @return the parentField
     */
    public String getParentField() {
        return parentField;
    }


    /**
     * @return the leftJoin
     */
    public boolean isLeftJoin() {
        return leftJoin;
    }


    /**
     * @param leftJoin the leftJoin to set
     */
    public void setLeftJoin(boolean leftJoin) {
        this.leftJoin = leftJoin;
    }


    /**
     * @param tableAlias the tableAlias to set
     */
    @Override
    public void setAlias(String tableAlias) {
        this.alias = tableAlias;
    }


    public boolean isIterationContext() {

        return iterationContext;
    }

    public void setIterationContext(boolean iterationContext) {
        this.iterationContext = iterationContext;
    }
}
