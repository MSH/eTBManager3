package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.enums.ExamStatus;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.Date;


/**
 * Base class to all laboratory exam results stored
 *
 * @author Ricardo Memoria
 */
@MappedSuperclass
public abstract class LaboratoryExam extends CaseEvent {

    public enum ExamResult { UNDEFINED, POSITIVE, NEGATIVE }

    @Column(length = 50)
    @PropertyLog(messageKey = "PatientSample.sampleNumber", operations = {Operation.NEW, Operation.DELETE})
    private String sampleNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ExamRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LABORATORY_ID")
    @PropertyLog(messageKey = "Laboratory", operations = {Operation.NEW, Operation.DELETE})
    private Laboratory laboratory;

    @Temporal(TemporalType.DATE)
    @PropertyLog(messageKey = "cases.exams.dateRelease", operations = {Operation.NEW})
    @Past
    private Date dateRelease;

    @Transient
    // Ricardo: TEMPORARY UNTIL A SOLUTION IS FOUND. Just to attend a request from the XML data model to
    // map an XML node to a property in the model
    private Integer clientId;

    private ExamStatus status;

    /**
     * Return a common way if the result is positive, negative or not informed
     *
     * @return
     */
    public abstract ExamResult getExamResult();

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * Check if the given date collected and sample number are the same as in the
     * exam data
     *
     * @param dateCollected the date sample was collected
     * @param sampleNumber  the given number of the sample collected
     * @return true if its the same as in the exam
     */
    public boolean isSameSample(Date dateCollected, String sampleNumber) {
        if (this.getDate() != dateCollected) {
            if ((this.getDate() == null) || (dateCollected == null)) {
                return false;
            }

            if (!this.getDate().equals(dateCollected)) {
                return false;
            }
        }

        if (this.sampleNumber == sampleNumber) {
            return true;
        }

        if ((this.sampleNumber == null) || (sampleNumber == null)) {
            return false;
        }

        return this.sampleNumber.equals(sampleNumber);
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String spnumber) {
        this.sampleNumber = spnumber;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
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

    public ExamRequest getRequest() {
        return request;
    }

    public void setRequest(ExamRequest request) {
        this.request = request;
    }

}
