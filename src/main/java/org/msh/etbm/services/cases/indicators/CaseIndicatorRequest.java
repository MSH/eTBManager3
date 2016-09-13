package org.msh.etbm.services.cases.indicators;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 10/9/16.
 */
public class CaseIndicatorRequest {

    private UUID adminUnitId;
    private UUID unitId;

    private Map<String, Object> filters;

    private List<String> columnVariables;
    private List<String> rowVariables;

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
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
