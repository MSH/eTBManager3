package org.msh.etbm.services.cases;

import java.util.UUID;

/**
 * Created by Mauricio on 13/09/2016.
 */
public class CaseActionResponse {
    UUID caseId;
    String caseDisplayString;

    public CaseActionResponse() {

    }

    public CaseActionResponse(UUID caseId, String caseDisplayString) {
        this.caseId = caseId;
        this.caseDisplayString = caseDisplayString;
    }

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public String getCaseDisplayString() {
        return caseDisplayString;
    }

    public void setCaseDisplayString(String caseDisplayString) {
        this.caseDisplayString = caseDisplayString;
    }
}
