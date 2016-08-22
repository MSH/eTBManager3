package org.msh.etbm.commons.sqlquery;

import java.util.Map;

/**
 * Created by rmemoria on 22/8/16.
 */
public interface RowMapper<E> {

    E map(Map<String, Object> values);
}
