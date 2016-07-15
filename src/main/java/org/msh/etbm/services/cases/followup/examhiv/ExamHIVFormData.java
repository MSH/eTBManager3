package org.msh.etbm.services.cases.followup.examhiv;

import org.msh.etbm.db.enums.HIVResult;
import org.msh.etbm.services.cases.followup.data.CaseEventFormData;

import java.util.Date;
import java.util.Optional;

/**
 * Created by msantos on 14/7/16.
 */
public class ExamHIVFormData extends CaseEventFormData {

    private Optional<HIVResult> result;
    private Optional<Date> startedARTdate;
    private Optional<Date> startedCPTdate;
    private Optional<String> laboratory;

    public Optional<HIVResult> getResult() {
        return result;
    }

    public void setResult(Optional<HIVResult> result) {
        this.result = result;
    }

    public Optional<Date> getStartedARTdate() {
        return startedARTdate;
    }

    public void setStartedARTdate(Optional<Date> startedARTdate) {
        this.startedARTdate = startedARTdate;
    }

    public Optional<Date> getStartedCPTdate() {
        return startedCPTdate;
    }

    public void setStartedCPTdate(Optional<Date> startedCPTdate) {
        this.startedCPTdate = startedCPTdate;
    }

    public Optional<String> getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Optional<String> laboratory) {
        this.laboratory = laboratory;
    }
}
