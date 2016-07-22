package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.ValidationState;
import org.msh.etbm.services.admin.AddressData;
import org.msh.etbm.services.admin.regimens.RegimenData;
import org.msh.etbm.services.admin.units.data.UnitData;

import java.util.Date;
import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
public class CaseData {

    private UUID id;

    private PatientData patient;

    private String registrationCode;

    private Date registrationDate;

    private String caseCode;

    private Date diagnosisDate;

    private CaseClassification classification;

    private DiagnosisType diagnosisType;

    private Integer age;

    private String outcome;

    private String otherOutcome;

    private Date outcomeDate;

    private CaseState state;

    private Period treatmentPeriod;

    private RegimenData regimen;

    private UnitData ownerUnit;

    private ValidationState validationState;

    private AddressData notifAddress;

    private AddressData currentAddress;

    private boolean notifAddressChanged;

    private String phoneNumber;

    private String mobileNumber;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PatientData getPatient() {
        return patient;
    }

    public void setPatient(PatientData patient) {
        this.patient = patient;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public CaseClassification getClassification() {
        return classification;
    }

    public void setClassification(CaseClassification classification) {
        this.classification = classification;
    }

    public DiagnosisType getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(DiagnosisType diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Date getOutcomeDate() {
        return outcomeDate;
    }

    public void setOutcomeDate(Date outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public CaseState getState() {
        return state;
    }

    public void setState(CaseState state) {
        this.state = state;
    }

    public Period getTreatmentPeriod() {
        return treatmentPeriod;
    }

    public void setTreatmentPeriod(Period treatmentPeriod) {
        this.treatmentPeriod = treatmentPeriod;
    }

    public RegimenData getRegimen() {
        return regimen;
    }

    public void setRegimen(RegimenData regimen) {
        this.regimen = regimen;
    }

    public UnitData getOwnerUnit() {
        return ownerUnit;
    }

    public void setOwnerUnit(UnitData ownerUnit) {
        this.ownerUnit = ownerUnit;
    }

    public ValidationState getValidationState() {
        return validationState;
    }

    public void setValidationState(ValidationState validationState) {
        this.validationState = validationState;
    }

    public String getDisplayString() {
        return "(" + classification + ") " + patient.getDisplayString();
    }
}
