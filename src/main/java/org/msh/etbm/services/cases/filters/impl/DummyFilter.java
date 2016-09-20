package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.Map;

/**
 * Created by rmemoria on 18/8/16.
 */
public class DummyFilter extends AbstractFilter {
    public DummyFilter(FilterGroup group, String id, String label) {
        super(label);
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        //
    }

    @Override
    public String getFilterType() {
        return "select";
    }
}
