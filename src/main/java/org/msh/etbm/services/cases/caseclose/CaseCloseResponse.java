package org.msh.etbm.services.cases.caseclose;

import org.msh.etbm.services.cases.CaseActionResponse;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 26/07/2016.
 */
public class CaseCloseResponse extends CaseActionResponse {

    Date outcomeDate;
    String outcome;
    String otherOutcome;

    public CaseCloseResponse(UUID tbcaseId, String tbcaseDisplayString, Date outcomeDate, String outcome, String otherOutcome) {
        super(tbcaseId, tbcaseDisplayString);
        this.outcomeDate = outcomeDate;
        this.outcome = outcome;
        this.otherOutcome = otherOutcome;
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
