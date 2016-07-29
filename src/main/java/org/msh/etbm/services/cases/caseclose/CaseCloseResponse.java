package org.msh.etbm.services.cases.caseclose;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 26/07/2016.
 */
public class CaseCloseResponse {
    UUID tbcaseId;
    String tbcaseDisplayString;
    Date outcomeDate;
    String outcome;
    String otherOutcome;

    public CaseCloseResponse(UUID tbcaseId, String tbcaseDisplayString, Date outcomeDate, String outcome, String otherOutcome) {
        this.tbcaseId = tbcaseId;
        this.tbcaseDisplayString = tbcaseDisplayString;
        this.outcomeDate = outcomeDate;
        this.outcome = outcome;
        this.otherOutcome = otherOutcome;
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

    public Date getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(Date outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOtherOutcome() {
        return otherOutcome;
    }

    public void setOtherOutcome(String otherOutcome) {
        this.otherOutcome = otherOutcome;
    }
}
