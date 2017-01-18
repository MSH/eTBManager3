package org.msh.etbm.services.dashboard;

import org.msh.etbm.services.RequestScope;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by rmemoria on 22/10/16.
 */
public class DashboardRequest implements Serializable {

    /**
     * The scope of the dashboard request (by workspace, admin unit or unit)
     */
    @NotNull
    private RequestScope scope;

    /**
     * The ID of the scope, if admin unit or unit
     */
    private UUID scopeId;


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
}
