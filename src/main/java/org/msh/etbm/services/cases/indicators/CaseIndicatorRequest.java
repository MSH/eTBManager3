package org.msh.etbm.services.cases.indicators;

import org.msh.etbm.services.RequestScope;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Request to generate a new indicator from cases
 * Created by rmemoria on 10/9/16.
 */
public class CaseIndicatorRequest {

    @NotNull
    private RequestScope scope;

    private UUID scopeId;

    private Map<String, Object> filters;

    private List<String> columnVariables;
    private List<String> rowVariables;

    public RequestScope getScope() {
        return scope;
    }

    public void setScope(RequestScope scope) {
        this.scope = scope;
    }

    public UUID getScopeId() {
        return scopeId;
    }

    public void setScopeId(UUID scopeId) {
        this.scopeId = scopeId;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public List<String> getColumnVariables() {
        return columnVariables;
    }

    public void setColumnVariables(List<String> columnVariables) {
        this.columnVariables = columnVariables;
    }

    public List<String> getRowVariables() {
        return rowVariables;
    }

    public void setRowVariables(List<String> rowVariables) {
        this.rowVariables = rowVariables;
    }
}
