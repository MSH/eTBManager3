package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.services.cases.filters.Filter;
import org.msh.etbm.services.cases.filters.FilterContext;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.Map;

/**
 * Simple filter implementation to act as an starting point implementation
 *
 * Created by rmemoria on 17/8/16.
 */
public abstract class AbstractFilter implements Filter {

    private String id;
    private String label;
    private FilterGroup group;

    public AbstractFilter(FilterGroup group, String id, String label) {
        super();
        this.group = group;
        this.id = id;
        this.label = label;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public FilterGroup getGroup() {
        return group;
    }

    @Override
    public Map<String, Object> getResources(FilterContext context, Map<String, Object> params) {
        return null;
    }
}
