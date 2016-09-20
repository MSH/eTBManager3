package org.msh.etbm.services.cases.summary;

import org.msh.etbm.services.RequestScope;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by rmemoria on 17/9/16.
 */
public class SummaryRequest {

    /**
     * The scope of the summary (workspace, admin unit or unit)
     */
    @NotNull
    private RequestScope scope;

    /**
     * The ID of the admin unit or unit, depending on the value in scope
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
