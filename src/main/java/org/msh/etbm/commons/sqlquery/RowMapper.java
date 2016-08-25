package org.msh.etbm.commons.sqlquery;

/**
 * Interface exposed by {@link SQLQueryExec} to read the content of the table row
 *
 * Created by rmemoria on 22/8/16.
 */
public interface RowMapper<E> {

    /**
     * Map the content of the row available from the {@link RowReader} to an object
     * instance returned by the method
     * @param reader the instance of {@link RowReader}
     * @return the instance of E created and mapped with the row values
     */
    E map(RowReader reader);
}
