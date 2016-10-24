package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.sqlquery.QueryDefs;

/**
 * Must be implemented in filter value iteration. It is used by {@link AbstractFilter#iterateValues(QueryDefs, Object)}
 * to abstract from the number of elements in the given value
 * Created by rmemoria on 22/10/16.
 */
public interface ValueIterator {

    void iterate(Object value);
}
