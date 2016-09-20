package org.msh.etbm.services.cases.tag;

import org.msh.etbm.services.RequestScope;

import java.util.UUID;

/**
 * Request to generate the tags report
 *
 * Created by rmemoria on 18/9/16.
 */
public class TagsReportRequest {
    private RequestScope scope;
    private UUID scopeId;

    public TagsReportRequest() {
    }

    public TagsReportRequest(RequestScope scope, UUID scopeId) {
        this.scope = scope;
        this.scopeId = scopeId;
    }

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
