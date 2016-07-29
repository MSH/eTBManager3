package org.msh.etbm.services.cases.caseclose;

import org.msh.etbm.db.enums.CaseState;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 26/07/2016.
 */
public class ReopenCaseResponse {
    UUID tbcaseId;
    String tbcaseDisplayString;
    CaseState state;

    public ReopenCaseResponse(UUID tbcaseId, String tbcaseDisplayString, CaseState state) {
        this.tbcaseId = tbcaseId;
        this.tbcaseDisplayString = tbcaseDisplayString;
        this.state = state;
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

    public CaseState getState() {
        return state;
    }

    public void setState(CaseState state) {
        this.state = state;
    }
}
