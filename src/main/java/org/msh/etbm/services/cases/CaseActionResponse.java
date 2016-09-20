package org.msh.etbm.services.cases;

import java.util.UUID;

/**
 * Created by Mauricio on 13/09/2016.
 */
public class CaseActionResponse {
    UUID tbcaseId;
    String tbcaseDisplayString;

    public CaseActionResponse() {

    }

    public CaseActionResponse(UUID tbcaseId, String tbcaseDisplayString) {
        this.tbcaseId = tbcaseId;
        this.tbcaseDisplayString = tbcaseDisplayString;
    }

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }

    public String getTbcaseDisplayString() {
        return tbcaseDisplayString;
    }

    public void setTbcaseDisplayString(String tbcaseDisplayString) {
        this.tbcaseDisplayString = tbcaseDisplayString;
    }
}
