package org.msh.etbm.services.cases.followup.data;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.entities.ExamRequest;
import org.msh.etbm.db.enums.ExamStatus;
import org.msh.etbm.services.cases.CaseEventData;

import java.util.Date;

/**
 * Created by msantos on 13/7/16.
 */
public abstract class LaboratoryExamData extends CaseEventData {

    private String sampleNumber;
    private ExamRequest request;
    private SynchronizableItem laboratory;
    private Date dateRelease;
    private ExamStatus status;

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public ExamRequest getRequest() {
        return request;
    }

    public void setRequest(ExamRequest request) {
        this.request = request;
    }

    public SynchronizableItem getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(SynchronizableItem laboratory) {
        this.laboratory = laboratory;
    }

    public Date getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(Date dateRelease) {
        this.dateRelease = dateRelease;
    }

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }
}
