package org.msh.etbm.services.cases.followup.examhiv;

import org.msh.etbm.db.enums.HIVResult;
import org.msh.etbm.services.cases.followup.data.CaseEventData;

import java.util.Date;

/**
 * Created by msantos on 14/7/16.
 */
public class ExamHIVData extends CaseEventData {

    private HIVResult result;
    private Date startedARTdate;
    private Date startedCPTdate;
    private String laboratory;

    public HIVResult getResult() {
        return result;
    }

    public void setResult(HIVResult result) {
        this.result = result;
    }

    public Date getStartedARTdate() {
        return startedARTdate;
    }

    public void setStartedARTdate(Date startedARTdate) {
        this.startedARTdate = startedARTdate;
    }

    public Date getStartedCPTdate() {
        return startedCPTdate;
    }

    public void setStartedCPTdate(Date startedCPTdate) {
        this.startedCPTdate = startedCPTdate;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }
}
