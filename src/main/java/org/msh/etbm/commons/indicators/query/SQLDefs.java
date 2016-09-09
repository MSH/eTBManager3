package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.indicators.FilterValue;


/**
 * Generic SQL definitions used by report instances of {@link org.msh.etbm.commons.indicators.filters.Filter} and
 * {@link org.msh.etbm.commons.indicators.variables.Variable} to
 * construct the SQL query to be sent to the database
 * @author Ricardo Memoria
 *
 */
public interface SQLDefs {

    /**
     * Return the name of the master table of the query
     * @return
     */
    TableJoin getMasterTable();

    /**
     * Return the table join equiv
     * @param table
     * @return
     */
    TableJoin table(String table);

    /**
     * Create a join between a parent table (already existing table in the
     * SQL definition) and a new target table
     * @param newtable is the table and field name of the new table to join with
     * @return parentTable is the table and field name of the existing table to join with
     */
    TableJoin join(String newtable, String parentTable);

    TableJoin join(String newtable, String newfield, String parentTable, String parentField);


    /**
     * Add a field of the master table to be returned by the query
     * @param field is the field to be returned
     */
    void select(String field);

    /**
     * Add a SQL restriction to the query
     * @param restriction
     */
    void addRestriction(String restriction);

    /**
     * Add a value to a parameter in the query
     * @param paramname
     * @param value
     */
    void addParameter(String paramname, Object value);


    /**
     * Return the value of a filter. If filter is not in the query, so it returns null
     * @param fielterid is the ID of the filter
     * @return instance of the {@link FilterValue} containing its value, or null if filter was not found
     */
    FilterValue getFilterValue(String fielterid);
}
