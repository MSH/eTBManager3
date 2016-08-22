package org.msh.etbm.commons.sqlquery;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by rmemoria on 22/8/16.
 */
public class SQLQueryExec {

    public <E> List<E> query(DataSource dataSource, RowMapper<E> mapper) {
        return null;
    }
}
