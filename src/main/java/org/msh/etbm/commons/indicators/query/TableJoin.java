package org.msh.etbm.commons.indicators.query;


/**
 * Store temporary information about a join between two tables. This information is primarily
 * used when generating a SQL declaration where it's necessary to create join between tables.
 * <p/>
 * This join structure is similar to a tree, where the root table join represents the main
 * class in the SQL from declaration
 *
 * @author Ricardo Memoria
 *
 */
public interface TableJoin {
    TableJoin select(String field);

    /**
     * Add a join to the current table
     * @param field is the field of the table to be joined with
     * @param targetTable is the table to join with
     * @return instance of the new {@link TableJoin} implementation
     */
    TableJoin join(String field, String targetTable);

    /**
     * Add a left join to the current table
     * @param field is the field of the current table to be joined with
     * @param targetTable is the table.field definition of the new table to join with
     * @return is the instance of the new {@link TableJoin} implementation
     */
    TableJoin leftJoin(String field, String targetTable);

    /**
     * Get the alias of the table to be used in the SQL statement
     * @return String value
     */
    String getAlias();

    /**
     * Change the alias of the table to be used in the SQL statement
     * @param value the new table alias
     */
    void setAlias(String value);


    /**
     * Return the table name of this join
     * @return String value
     */
    String getTableName();

    /**
     * Return the parent join of this table join
     * @return instance of {@link TableJoin} implementation
     */
    TableJoin getParentJoin();
}
