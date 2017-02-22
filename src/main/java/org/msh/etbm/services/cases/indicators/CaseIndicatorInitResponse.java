package org.msh.etbm.services.cases.indicators;

import org.msh.etbm.commons.filters.FilterGroupData;
import org.msh.etbm.commons.indicators.variables.VariableGroupData;

import java.util.List;

/**
 * Created by rmemoria on 7/10/16.
 */
public class CaseIndicatorInitResponse {

    private List<FilterGroupData> filters;

    private List<VariableGroupData> variables;

    public List<FilterGroupData> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterGroupData> filters) {
        this.filters = filters;
    }

    public List<VariableGroupData> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableGroupData> variables) {
        this.variables = variables;
    }
}
