package org.msh.etbm.services.cases.followup.data;

import org.msh.etbm.db.entities.ExamRequest;
import org.msh.etbm.db.enums.ExamStatus;

import java.util.Date;
import java.util.UUID;

/**
 * Created by msantos on 13/7/16.
 */
public abstract class LaboratoryExamData extends CaseEventData {

    private String sampleNumber;
    private ExamRequest request;
    private UUID laboratoryId;
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

    public UUID getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(UUID laboratoryId) {
        this.laboratoryId = laboratoryId;
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
