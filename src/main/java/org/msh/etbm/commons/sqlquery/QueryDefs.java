package org.msh.etbm.commons.sqlquery;

/**
 * Created by rmemoria on 15/8/16.
 */
public interface QueryDefs {

    /**
     * Add a restriction to the SQL statement that will be included in the WHERE clause
     * @param sqlexpr the SQL expression that will be included in the where clause
     * @return
     */
    QueryDefs restrict(String sqlexpr);

    /**
     * Add a restriction to the SQL statement that will be included in the WHERE clause,
     * with the possibility to also include parameters that will replace the ? in the
     * given SQL expression
     * @param sqlexpr the SQL expression that will be included in the where clause
     * @return
     */
    QueryDefs restrict(String sqlexpr, Object... parameters);

    /**
     * Include a table and its relation (on) to be added as a join operation in the SQL. When declaring
     * the ON expression, you may declare fields "table.fieldName", where table may be
     * @param tableName the table name
     * @param on the ON SQL instruction of the JOIN operation
     * @return
     */
    QueryDefs join(String tableName, String on);


    /**
     * Include a table and its relation (sql ON) in the SQL declaration as a left join.
     * @param tableName the table name to be included
     * @param on the ON SQL statement of the LEFT JOIN operation
     * @return
     */
    QueryDefs leftJoin(String tableName, String on);

    /**
     * Add the fields to be included in the SQL SELECT statement
     * @param fields the list of fields separated by comma
     * @return
     */
    QueryDefs select(String fields);
}
