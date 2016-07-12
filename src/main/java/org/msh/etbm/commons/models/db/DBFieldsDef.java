package org.msh.etbm.commons.models.db;

/**
 * Interface used in the construction of the SQL select to specify fields
 * and tables to use. When declaring join operations, you may specify fields using "tableName.fieldName",
 * where tableName is:
 * <ul>
 *     <li>
 *         The name of a table in the query;
 *     </li>
 *     <li>
 *         $this to reference the own table in the join
 *     </li>
 *     <li>
 *         $parent to reference the parent table of the join
 *     </li>
 *     <li>
 *         $root to reference the main table in the query
 *     </li>
 * </ul>
 *
 * Created by rmemoria on 7/7/16.
 */
public interface DBFieldsDef {

    DBFieldsDef add(String fieldName);

    /**
     * Include a table and its relation (on) to be added as a join operation in the SQL. When declaring
     * the ON expression, you may declare fields "table.fieldName", where table may be
     * @param tableName the table name
     * @param on the ON SQL instruction of the JOIN operation
     * @return
     */
    DBFieldsDef join(String tableName, String on);

    DBFieldsDef leftJoin(String tableName, String on);

    String getRootTable();
}
