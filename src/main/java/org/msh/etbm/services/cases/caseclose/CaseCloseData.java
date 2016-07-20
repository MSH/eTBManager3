package org.msh.etbm.services.cases.caseclose;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 19/07/2016.
 */
public class CaseCloseData {

    private Date outcomeDate;

    private String outcome;

    private String otherOutcome;

    private UUID tbcaseId;

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

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }
}
