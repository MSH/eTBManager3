package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.Item;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.admin.units.data.UnitData;

import java.util.Date;

/**
 * Stores information about patient search of new notification flush
 * Created by Mauricio on 30/08/2016.
 */
public class PatientSearchItem {

    private PatientData patient;

    private String caseNumber;

    private Item<CaseClassification> classification;

    private Item<DiagnosisType> diagnosisType;

    private Item<CaseState> state;

    private UnitData notificationUnit;

    private Date registrationDate;

    private Date diagnosisDate;

    private Date iniTreatmentDate;

    private Date outcomeDate;

    private String outcome;

    public PatientData getPatient() {
        return patient;
    }

    public void setPatient(PatientData patient) {
        this.patient = patient;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Item<CaseClassification> getClassification() {
        return classification;
    }

    public void setClassification(Item<CaseClassification> classification) {
        this.classification = classification;
    }

    public Item<DiagnosisType> getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(Item<DiagnosisType> diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public Item<CaseState> getState() {
        return state;
    }

    public void setState(Item<CaseState> state) {
        this.state = state;
    }

    public UnitData getNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(UnitData notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public Date getIniTreatmentDate() {
        return iniTreatmentDate;
    }

    public void setIniTreatmentDate(Date iniTreatmentDate) {
        this.iniTreatmentDate = iniTreatmentDate;
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
}
