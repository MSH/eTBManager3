package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.services.RequestScope;

import java.util.UUID;

/**
 * A value of a filter used in a {@link ScopeFilter}
 *
 * Created by rmemoria on 20/10/16.
 */
public class ScopeFilterValue {

    private RequestScope scope;
    private UUID value;

    private String workspaceFieldName;
    private String unitFieldName;
    private String adminUnitTable;

    public ScopeFilterValue() {
    }

    public ScopeFilterValue(RequestScope scope, UUID value, String workspaceFieldName, String unitFieldName, String adminUnitTable) {
        this.scope = scope;
        this.value = value;
        this.workspaceFieldName = workspaceFieldName;
        this.unitFieldName = unitFieldName;
        this.adminUnitTable = adminUnitTable;
    }

    public String getWorkspaceFieldName() {
        return workspaceFieldName;
    }

    public void setWorkspaceFieldName(String workspaceFieldName) {
        this.workspaceFieldName = workspaceFieldName;
    }

    public String getUnitFieldName() {
        return unitFieldName;
    }

    public void setUnitFieldName(String unitFieldName) {
        this.unitFieldName = unitFieldName;
    }

    public String getAdminUnitTable() {
        return adminUnitTable;
    }

    public void setAdminUnitTable(String adminUnitTable) {
        this.adminUnitTable = adminUnitTable;
    }

    public RequestScope getScope() {
        return scope;
    }

    public void setScope(RequestScope scope) {
        this.scope = scope;
    }

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }
}
