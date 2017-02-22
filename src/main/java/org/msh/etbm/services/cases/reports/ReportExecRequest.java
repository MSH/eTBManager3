package org.msh.etbm.services.cases.reports;

import org.msh.etbm.services.RequestScope;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request to execute a report
 * Created by rmemoria on 20/10/16.
 */
public class ReportExecRequest {

    @NotNull
    private RequestScope scope;

    private UUID scopeId;

    @NotNull
    private UUID reportId;

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

    public UUID getReportId() {
        return reportId;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }
}
