package org.msh.etbm.web.api.cases;

import java.util.UUID;

/**
 * Standard request data for case forms
 *
 * Created by rmemoria on 31/12/16.
 */
public class CaseFormRequest {

    private UUID id;
    private UUID caseId;
    private boolean readOnly;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
