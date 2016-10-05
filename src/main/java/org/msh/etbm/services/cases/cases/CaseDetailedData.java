package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.admin.AddressData;
import org.msh.etbm.services.admin.regimens.RegimenData;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.cases.comments.CaseCommentData;
import org.msh.etbm.services.cases.issues.IssueData;
import org.msh.etbm.services.cases.patient.PatientDetailedData;
import org.msh.etbm.services.cases.tag.CaseTagsData;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
public class CaseDetailedData {

    private UUID id;

    private PatientDetailedData patient;

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

    private UnitData transferOutUnit;

    private UnitData notificationUnit;

    private boolean transferring;

    private boolean validated;

    private AddressData notifAddress;

    private AddressData currentAddress;

    private boolean notifAddressChanged;

    private String phoneNumber;

    private String mobileNumber;

    // Risk Factors and Concomitant Diagnoses
    private boolean alcoholExcessiveUse;
    private boolean tobaccoUseWithin;
    private boolean aids;
    private boolean diabetes;
    private boolean anaemia;
    private boolean malnutrition;

    private List<CaseTagsData> tags;

    private List<CaseCommentData> comments;

    private List<IssueData> issues;

    private List<CaseItem> allCases;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PatientDetailedData getPatient() {
        return patient;
    }

    public void setPatient(PatientDetailedData patient) {
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

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public AddressData getNotifAddress() {
        return notifAddress;
    }

    public void setNotifAddress(AddressData notifAddress) {
        this.notifAddress = notifAddress;
    }

    public AddressData getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(AddressData currentAddress) {
        this.currentAddress = currentAddress;
    }

    public boolean isNotifAddressChanged() {
        return notifAddressChanged;
    }

    public void setNotifAddressChanged(boolean notifAddressChanged) {
        this.notifAddressChanged = notifAddressChanged;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isAlcoholExcessiveUse() {
        return alcoholExcessiveUse;
    }

    public void setAlcoholExcessiveUse(boolean alcoholExcessiveUse) {
        this.alcoholExcessiveUse = alcoholExcessiveUse;
    }

    public boolean isTobaccoUseWithin() {
        return tobaccoUseWithin;
    }

    public void setTobaccoUseWithin(boolean tobaccoUseWithin) {
        this.tobaccoUseWithin = tobaccoUseWithin;
    }

    public boolean isAids() {
        return aids;
    }

    public void setAids(boolean aids) {
        this.aids = aids;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isAnaemia() {
        return anaemia;
    }

    public void setAnaemia(boolean anaemia) {
        this.anaemia = anaemia;
    }

    public boolean isMalnutrition() {
        return malnutrition;
    }

    public void setMalnutrition(boolean malnutrition) {
        this.malnutrition = malnutrition;
    }

    public List<CaseTagsData> getTags() {
        return tags;
    }

    public void setTags(List<CaseTagsData> tags) {
        this.tags = tags;
    }

    public List<CaseCommentData> getComments() {
        return comments;
    }

    public void setComments(List<CaseCommentData> comments) {
        this.comments = comments;
    }

    public List<IssueData> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueData> issues) {
        this.issues = issues;
    }

    public UnitData getNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(UnitData notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public boolean isTransferring() {
        return transferring;
    }

    public void setTransferring(boolean transferring) {
        this.transferring = transferring;
    }

    public UnitData getTransferOutUnit() {
        return transferOutUnit;
    }

    public void setTransferOutUnit(UnitData transferOutUnit) {
        this.transferOutUnit = transferOutUnit;
    }

    public List<CaseItem> getAllCases() {
        return allCases;
    }

    public void setAllCases(List<CaseItem> allCases) {
        this.allCases = allCases;
    }
}
