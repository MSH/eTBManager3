package org.msh.etbm.services.cases.cases.data;

import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.InfectionSite;
import org.msh.etbm.services.admin.AddressData;
import org.msh.etbm.services.admin.regimens.RegimenData;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.cases.comments.CaseCommentData;
import org.msh.etbm.services.cases.comorbidity.CaseComorbiditiesData;
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

    private String caseNumber;

    private Date diagnosisDate;

    private CaseClassification classification;

    private DiagnosisType diagnosisType;

    private String registrationGroup;

    private InfectionSite infectionSite;

    private String nationality;

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

    private CaseComorbiditiesData comorbidities;

    private Date moveDate;

    private String extrapulmonaryType;
    private String extrapulmonaryType2;
    private String pulmonaryType;

    private String drugResistanceType;

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

    public CaseComorbiditiesData getComorbidities() {
        return comorbidities;
    }

    public void setComorbidities(CaseComorbiditiesData comorbidities) {
        this.comorbidities = comorbidities;
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

    public Date getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(Date moveDate) {
        this.moveDate = moveDate;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getRegistrationGroup() {
        return registrationGroup;
    }

    public void setRegistrationGroup(String registrationGroup) {
        this.registrationGroup = registrationGroup;
    }

    public InfectionSite getInfectionSite() {
        return infectionSite;
    }

    public void setInfectionSite(InfectionSite infectionSite) {
        this.infectionSite = infectionSite;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getExtrapulmonaryType() {
        return extrapulmonaryType;
    }

    public void setExtrapulmonaryType(String extrapulmonaryType) {
        this.extrapulmonaryType = extrapulmonaryType;
    }

    public String getExtrapulmonaryType2() {
        return extrapulmonaryType2;
    }

    public void setExtrapulmonaryType2(String extrapulmonaryType2) {
        this.extrapulmonaryType2 = extrapulmonaryType2;
    }

    public String getPulmonaryType() {
        return pulmonaryType;
    }

    public void setPulmonaryType(String pulmonaryType) {
        this.pulmonaryType = pulmonaryType;
    }

    public String getDrugResistanceType() {
        return drugResistanceType;
    }

    public void setDrugResistanceType(String drugResistanceType) {
        this.drugResistanceType = drugResistanceType;
    }
}
