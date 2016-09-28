package org.msh.etbm.services.cases.reports;

import org.msh.etbm.services.RequestScope;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by rmemoria on 23/9/16.
 */
public class ReportRequest {

    @NotNull
    private RequestScope scope;
    private UUID id;

    public RequestScope getScope() {
        return scope;
    }

    public void setScope(RequestScope scope) {
        this.scope = scope;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
