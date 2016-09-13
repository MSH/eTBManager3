package org.msh.etbm.commons.sqlquery;

/**
 * Interfaced passed to {@link SQLQueryBuilder} in order to listener to specific injection
 * operations during query building using the {@link QueryDefs} interface
 * Created by rmemoria on 10/9/16.
 */
public interface QueryDefsListener {

    /**
     * Called when a new field is injected in the query
     * @param field the object containing information about the field
     */
    void onInjectedField(SQLField field);
}
