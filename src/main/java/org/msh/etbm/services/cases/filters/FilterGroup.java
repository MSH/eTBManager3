package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.filters.FilterItem;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 4/10/16.
 */
public class FilterGroup {

    private String label;
    private List<FilterItem> filters = new ArrayList<>();
    private List<Variable> variables = new ArrayList<>();

    public FilterGroup(String label) {
        this.label = label;
    }

    public void add(Object item) {
        if (item instanceof FilterItem) {
            addFilter((FilterItem)item);
        }

        if (item instanceof Variable) {
            addVariable((Variable)item);
        }
    }

    public void addVariable(Variable var) {
        variables.add(var);
    }

    public void addFilter(FilterItem filter) {
        filters.add(filter);
    }

    public List<FilterItem> getFilters() {
        return filters;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public String getLabel() {
        return label;
    }
}
