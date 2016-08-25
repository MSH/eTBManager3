package org.msh.etbm.services.cases.followup.data;

import org.msh.etbm.db.entities.ExamRequest;
import org.msh.etbm.db.enums.ExamStatus;
import org.msh.etbm.services.cases.CaseEventFormData;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by msantos on 13/7/16.
 */
public abstract class LaboratoryExamFormData extends CaseEventFormData {

    private Optional<String> sampleNumber;
    private Optional<ExamRequest> request;
    private Optional<UUID> laboratoryId;
    private Optional<Date> dateRelease;
    private Optional<ExamStatus> status;

    public Optional<String> getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(Optional<String> sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public Optional<ExamRequest> getRequest() {
        return request;
    }

    public void setRequest(Optional<ExamRequest> request) {
        this.request = request;
    }

    public Optional<UUID> getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Optional<UUID> laboratoryId) {
        this.laboratoryId = laboratoryId;
    }

    public Optional<Date> getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(Optional<Date> dateRelease) {
        this.dateRelease = dateRelease;
    }

    public Optional<ExamStatus> getStatus() {
        return status;
    }

    public void setStatus(Optional<ExamStatus> status) {
        this.status = status;
    }
}
