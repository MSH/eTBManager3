package org.msh.etbm.db.entities;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.Address;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.*;


/**
 * Store information about a case (TB or DR-TB)
 *
 * @author Ricardo Memoria
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tbcase")
public class TbCase extends WorkspaceEntity {

    @PropertyLog(messageKey = "CaseClassification")
    private CaseClassification suspectClassification;

    /**
     * The code of the patient at the moment of registration (presumptive code)
     */
    @Column(length = 50)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String registrationNumber;

    private String caseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT_ID")
    @NotNull
    @PropertyLog(operations = {Operation.ALL})
    private Patient patient;

    private Integer age;

    /**
     * The date the patient was registered in the unit. If before the diagnosis date, this period
     * between this date and the diagnosis date is considered the period the patient was a suspect
     */
    @NotNull
    @Temporal(TemporalType.DATE)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private Date registrationDate;

    @Temporal(TemporalType.DATE)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private Date diagnosisDate;

    @Temporal(TemporalType.DATE)
    private Date outcomeDate;

    // Treatment information
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "iniDate", column = @Column(name = "iniTreatmentDate")),
            @AttributeOverride(name = "endDate", column = @Column(name = "endTreatmentDate"))
        })
    @PropertyLog(operations = {Operation.ALL}, addProperties = true)
    private Period treatmentPeriod = new Period();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGIMEN_ID")
    private Regimen regimen;

    private boolean movedToIndividualized;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_UNIT_ID")
    @NotNull
    private Tbunit ownerUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSFER_OUT_UNIT_ID")
    private Tbunit transferOutUnit;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase")
    @PropertyLog(ignore = true)
    private List<TreatmentHealthUnit> treatmentUnits = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase")
    @PropertyLog(ignore = true)
    private List<PrescribedMedicine> prescriptions = new ArrayList<>();

    @NotNull
    private CaseState state;

    @NotNull
    private boolean validated;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String registrationGroup;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private CaseDefinition caseDefinition;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private DiagnosisType diagnosisType;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE}, messageKey = "DrugResistanceType")
    private String drugResistanceType;

    @NotNull
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private CaseClassification classification;

    @PropertyLog(messageKey = "InfectionSite", operations = {Operation.NEW})
    private InfectionSite infectionSite;

    @Column(length = 50)
    private String pulmonaryType;

    @Column(length = 50)
    private String extrapulmonaryType;

    @Column(length = 50)
    private String extrapulmonaryType2;

    @Column(length = 100)
    private String registrationGroupOther;

    @Column(length = 50)
    @PropertyLog(messageKey = "Nationality")
    private String nationality;

    @Column(length = 50)
    private String outcome;

    /**
     * If true, case is being transferred to another unit
     */
    private boolean transferring;

    @Column(length = 100)
    private String otherOutcome;

    @Column(length = 50)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTIFICATION_UNIT_ID")
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private Tbunit notificationUnit;

    private boolean movedSecondLineTreatment;

    @Column(length = 100)
    private String patientContactName;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<CaseComment> comments = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "NOTIF_ADDRESS")),
            @AttributeOverride(name = "complement", column = @Column(name = "NOTIF_COMPLEMENT")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "NOTIF_ZIPCODE")),
        })
    @AssociationOverrides({
            @AssociationOverride(name = "adminUnit", joinColumns = @JoinColumn(name = "NOTIF_ADMINUNIT_ID"))
        })
    @PropertyLog(messageKey = "cases.details.addressnotif", operations = {Operation.NEW})
    private Address notifAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "CURR_ADDRESS")),
            @AttributeOverride(name = "complement", column = @Column(name = "CURR_COMPLEMENT")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "CURR_ZIPCODE")),
        })
    @AssociationOverrides({
            @AssociationOverride(name = "adminUnit", joinColumns = @JoinColumn(name = "CURR_ADMINUNIT_ID"))
        })
    @PropertyLog(messageKey = "cases.details.addresscurr")
    private Address currentAddress;

    @Column(name = "NOTIF_LOCALITYTYPE")
    private LocalityType localityType;

    @Column(length = 50)
    private String phoneNumber;

    @Column(length = 50)
    @PropertyLog(messageKey = "global.mobile")
    private String mobileNumber;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<CaseSideEffect> sideEffects = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @OrderBy("date desc")
    @PropertyLog(ignore = true)
    private List<MedicalExamination> examinations = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<ExamXRay> resXRay = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(messageKey = "cases.contacts")
    private List<CaseContact> contacts = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<TreatmentMonitoring> treatmentMonitoring = new ArrayList<>();

    // Risk Factors and Concomitant Diagnoses
    private boolean alcoholExcessiveUse;
    private boolean tobaccoUseWithin;
    private boolean aids;
    private boolean diabetes;
    private boolean anaemia;
    private boolean malnutrition;

    /* EXAMS */
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<ExamHIV> resHIV = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<ExamCulture> examsCulture = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<ExamMicroscopy> examsMicroscopy = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<ExamDST> examsDST = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<ExamXpert> examsXpert = new ArrayList<>();

    @PropertyLog(ignore = true)
    private SecDrugsReceived secDrugsReceived;

    @Temporal(TemporalType.DATE)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private Date lastBmuDateTbRegister;

    @Column(length = 50)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String lastBmuTbRegistNumber;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
    private List<Issue> issues = new ArrayList<>();

    /**
     * Tags of this case
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tags_case",
            joinColumns = {@JoinColumn(name = "CASE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TAG_ID")})
    @PropertyLog(ignore = true)
    private List<Tag> tags = new ArrayList<>();


    /**
     * Return number of month of treatment based on the date
     *
     * @param date
     * @return
     */
    public int getMonthTreatment(Date date) {
        if ((treatmentPeriod == null) || (date == null)) {
            return -1;
        }

        Date dtTreat = getTreatmentPeriod().getIniDate();
        if ((dtTreat == null) || (date.before(dtTreat))) {
            return -1;
        }

        return DateUtils.monthsBetween(date, dtTreat) + 1;
    }


    /**
     * Returns if the case is validated or not
     *
     * @return true - if the case is validated, false - otherwise
     */
    public boolean isValidated() {
        return this.validated;
    }

    /**
     * Returns if the case is a pulmonary TB/MDRTB
     *
     * @return - true if it is pulmonary or pulmonary/extrapulmonary
     */
    public boolean isPulmonary() {
        return (getInfectionSite() != null) && ((infectionSite == InfectionSite.PULMONARY) || (infectionSite == InfectionSite.BOTH));
    }


    /**
     * Returns if the case is a extrapulmonary TB/MDRTB
     *
     * @return - true if it is extrapulmonary or pulmonary/extrapulmonary
     */
    public boolean isExtrapulmonary() {
        return (getInfectionSite() != null) && ((infectionSite == InfectionSite.EXTRAPULMONARY) || (infectionSite == InfectionSite.BOTH));
    }

    /**
     * Search for side effect data by the side effect
     *
     * @param sideEffect - FieldValue object representing the side effect
     * @return - CaseSideEffect instance containing side effect data of the case, or null if there is no side effect data
     */
    public CaseSideEffect findSideEffectData(String sideEffect) {
        for (CaseSideEffect se : getSideEffects()) {
            if (se.getSideEffect().equals(sideEffect)) {
                return se;
            }
        }
        return null;
    }


    /**
     * @return the sideEffects
     */
    public List<CaseSideEffect> getSideEffects() {
        return sideEffects;
    }


    /**
     * @param sideEffects the sideEffects to set
     */
    public void setSideEffects(List<CaseSideEffect> sideEffects) {
        this.sideEffects = sideEffects;
    }


    /**
     * Formats the case number to be displayed to the user
     *
     * @param patientNumber - patient record number
     * @param caseNumber    - case number associated to the patient
     * @return - formated case number
     */
    static public String formatCaseNumber(int patientNumber, int caseNumber) {
        DecimalFormat df = new DecimalFormat("000");
        String s = df.format(patientNumber);

        return caseNumber > 1 ? s + "-" + Integer.toString(caseNumber) : s;
    }


    /**
     * Check if the case is open
     *
     * @return
     */
    public boolean isOpen() {
        return state != CaseState.CLOSED;
    }


    /**
     * Is case on treatment ?
     *
     * @return
     */
    public boolean isOnTreatment() {
        return state == CaseState.ONTREATMENT;
    }

    /**
     * Calculates the age based on birthDate and registrationDate
     * @return
     */
    public int getUpdatedAge() {
        Patient p = getPatient();
        if (p == null) {
            return 0;
        }

        Date dt = p.getBirthDate();
        Date dt2 = registrationDate;

        if (dt == null) {
            return 0;
        }

        if (dt2 == null) {
            dt2 = new Date();
        }

        return DateUtils.yearsBetween(dt, dt2);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public CaseState getState() {
        return state;
    }

    public void setState(CaseState state) {
        this.state = state;
    }

    public List<TreatmentHealthUnit> getTreatmentUnits() {
        return treatmentUnits;
    }

    public void setTreatmentUnits(List<TreatmentHealthUnit> treatmentUnits) {
        this.treatmentUnits = treatmentUnits;
    }

    public List<ExamHIV> getResHIV() {
        return resHIV;
    }


    public void setResHIV(List<ExamHIV> resHIV) {
        this.resHIV = resHIV;
    }


    public List<MedicalExamination> getExaminations() {
        return examinations;
    }


    public void setExaminations(List<MedicalExamination> examinations) {
        this.examinations = examinations;
    }


    public CaseClassification getClassification() {
        return classification;
    }


    public void setClassification(CaseClassification classification) {
        this.classification = classification;
    }


    public InfectionSite getInfectionSite() {
        return infectionSite;
    }


    public void setInfectionSite(InfectionSite infectionSite) {
        this.infectionSite = infectionSite;
    }

    public List<CaseComment> getComments() {
        return comments;
    }

    public void setComments(List<CaseComment> comments) {
        this.comments = comments;
    }

    public Address getNotifAddress() {
        if (notifAddress == null) {
            notifAddress = new Address();
        }

        return notifAddress;
    }


    public void setNotifAddress(Address notifAddress) {
        this.notifAddress = notifAddress;
    }


    public Address getCurrentAddress() {
        if (currentAddress == null) {
            currentAddress = new Address();
        }

        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
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

    public String getRegistrationGroup() {
        return registrationGroup;
    }

    public void setRegistrationGroup(String registrationGroup) {
        this.registrationGroup = registrationGroup;
    }

    public String getNationality() {
        return nationality;
    }


    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    public Tbunit getNotificationUnit() {
        return notificationUnit;
    }


    public void setNotificationUnit(Tbunit notificationUnit) {
        this.notificationUnit = notificationUnit;
    }


    public Date getDiagnosisDate() {
        return diagnosisDate;
    }


    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }


    public Date getOutcomeDate() {
        return outcomeDate;
    }


    public void setOutcomeDate(Date outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public String getRegistrationGroupOther() {
        return registrationGroupOther;
    }

    public void setRegistrationGroupOther(String registrationGroupOther) {
        this.registrationGroupOther = registrationGroupOther;
    }

    /**
     * @return the resXRay
     */
    public List<ExamXRay> getResXRay() {
        return resXRay;
    }


    /**
     * @param resXRay the resXRay to set
     */
    public void setResXRay(List<ExamXRay> resXRay) {
        this.resXRay = resXRay;
    }


    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }


    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }


    /**
     * @return the registrationDate
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @param registrationDate the registrationDate to set
     */
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }


    /**
     * @param diagnosisType the diagnosisType to set
     */
    public void setDiagnosisType(DiagnosisType diagnosisType) {
        this.diagnosisType = diagnosisType;
    }


    /**
     * @return the diagnosisType
     */
    public DiagnosisType getDiagnosisType() {
        return diagnosisType;
    }


    /**
     * @return the registrationCode
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }


    /**
     * @param registrationCode the registrationCode to set
     */
    public void setRegistrationNumber(String registrationCode) {
        this.registrationNumber = registrationCode;
    }


    /**
     * @return the drugResistanceType
     */
    public String getDrugResistanceType() {
        return drugResistanceType;
    }


    /**
     * @param drugResistanceType the drugResistanceType to set
     */
    public void setDrugResistanceType(String drugResistanceType) {
        this.drugResistanceType = drugResistanceType;
    }

    /**
     * @return the patientContactName
     */
    public String getPatientContactName() {
        return patientContactName;
    }


    /**
     * @param patientContactName the patientContactName to set
     */
    public void setPatientContactName(String patientContactName) {
        this.patientContactName = patientContactName;
    }


    /**
     * @return the contacts
     */
    public List<CaseContact> getContacts() {
        return contacts;
    }


    /**
     * @param contacts the contacts to set
     */
    public void setContacts(List<CaseContact> contacts) {
        this.contacts = contacts;
    }


    /**
     * @return the pulmonaryType
     */
    public String getPulmonaryType() {
        return pulmonaryType;
    }


    /**
     * @param pulmonaryType the pulmonaryType to set
     */
    public void setPulmonaryType(String pulmonaryType) {
        this.pulmonaryType = pulmonaryType;
    }


    /**
     * @return the extrapulmonaryType
     */
    public String getExtrapulmonaryType() {
        return extrapulmonaryType;
    }


    /**
     * @param extrapulmonaryType the extrapulmonaryType to set
     */
    public void setExtrapulmonaryType(String extrapulmonaryType) {
        this.extrapulmonaryType = extrapulmonaryType;
    }


    /**
     * @return the extrapulmonaryType2
     */
    public String getExtrapulmonaryType2() {
        return extrapulmonaryType2;
    }


    /**
     * @param extrapulmonaryType2 the extrapulmonaryType2 to set
     */
    public void setExtrapulmonaryType2(String extrapulmonaryType2) {
        this.extrapulmonaryType2 = extrapulmonaryType2;
    }



    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }


    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public List<PrescribedMedicine> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescribedMedicine> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public Period getTreatmentPeriod() {
        return treatmentPeriod;
    }


    public void setTreatmentPeriod(Period treatmentPeriod) {
        this.treatmentPeriod = treatmentPeriod;
    }



    public List<ExamCulture> getExamsCulture() {
        return examsCulture;
    }


    public void setExamsCulture(List<ExamCulture> examsCulture) {
        this.examsCulture = examsCulture;
    }


    public List<ExamMicroscopy> getExamsMicroscopy() {
        return examsMicroscopy;
    }


    public void setExamsMicroscopy(List<ExamMicroscopy> examsMicroscopy) {
        this.examsMicroscopy = examsMicroscopy;
    }


    public List<ExamDST> getExamsDST() {
        return examsDST;
    }


    public void setExamsDST(List<ExamDST> examsDST) {
        this.examsDST = examsDST;
    }


    /**
     * @return the regimen
     */
    public Regimen getRegimen() {
        return regimen;
    }


    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }


    /**
     * @return the ownerUnit
     */
    public Tbunit getOwnerUnit() {
        return ownerUnit;
    }


    /**
     * @param ownerUnit the ownerUnit to set
     */
    public void setOwnerUnit(Tbunit ownerUnit) {
        this.ownerUnit = ownerUnit;
    }

    public Tbunit getTransferOutUnit() {
        return transferOutUnit;
    }

    public void setTransferOutUnit(Tbunit transferOutUnit) {
        this.transferOutUnit = transferOutUnit;
    }

    /**
     * @return the tags
     */
    public List<Tag> getTags() {
        return tags;
    }


    /**
     * @param tags the tags to set
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }


    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    /**
     * @return the suspectClassification
     */
    public CaseClassification getSuspectClassification() {
        return suspectClassification;
    }


    /**
     * @param suspectClassification the suspectClassification to set
     */
    public void setSuspectClassification(CaseClassification suspectClassification) {
        this.suspectClassification = suspectClassification;
    }

    /**
     * @return the treatmentMonitoring
     */
    public List<TreatmentMonitoring> getTreatmentMonitoring() {
        return treatmentMonitoring;
    }

    /**
     * @param treatmentMonitoring the treatmentMonitoring to set
     */
    public void setTreatmentMonitoring(List<TreatmentMonitoring> treatmentMonitoring) {
        this.treatmentMonitoring = treatmentMonitoring;
    }

    public List<ExamXpert> getExamsXpert() {
        return examsXpert;
    }

    public void setExamsXpert(List<ExamXpert> examsXpert) {
        this.examsXpert = examsXpert;
    }

    public void setSecDrugsReceived(SecDrugsReceived secDrugsReceived) {
        this.secDrugsReceived = secDrugsReceived;
    }

    public SecDrugsReceived getSecDrugsReceived() {
        return this.secDrugsReceived;
    }

    public CaseDefinition getCaseDefinition() {
        return caseDefinition;
    }

    public void setCaseDefinition(CaseDefinition caseDefinition) {
        this.caseDefinition = caseDefinition;
    }

    public Date getLastBmuDateTbRegister() {
        return lastBmuDateTbRegister;
    }

    public void setLastBmuDateTbRegister(Date lastBmuDateTbRegister) {
        this.lastBmuDateTbRegister = lastBmuDateTbRegister;
    }

    public String getLastBmuTbRegistNumber() {
        return lastBmuTbRegistNumber;
    }

    public void setLastBmuTbRegistNumber(String lastBmuTbRegistNumber) {
        this.lastBmuTbRegistNumber = lastBmuTbRegistNumber;
    }

    public boolean isMovedSecondLineTreatment() {
        return movedSecondLineTreatment;
    }

    public void setMovedSecondLineTreatment(boolean movedSecondLineTreatment) {
        this.movedSecondLineTreatment = movedSecondLineTreatment;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public LocalityType getLocalityType() {
        return localityType;
    }

    public void setLocalityType(LocalityType localityType) {
        this.localityType = localityType;
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

    @Override
    public String getDisplayString() {
        return "(" + classification + ") " + patient.getDisplayString();
    }

    public boolean isMovedToIndividualized() {
        return movedToIndividualized;
    }

    public void setMovedToIndividualized(boolean movedToIndividualized) {
        this.movedToIndividualized = movedToIndividualized;
    }

    public boolean isTransferring() {
        return transferring;
    }

    public void setTransferring(boolean transferring) {
        this.transferring = transferring;
    }

    /**
     * Return list of treatment health units sorted by period
     * @return
     */
    public List<TreatmentHealthUnit> getSortedTreatmentHealthUnits() {
        // sort the periods
        Collections.sort(treatmentUnits, new Comparator<TreatmentHealthUnit>() {
            public int compare(TreatmentHealthUnit o1, TreatmentHealthUnit o2) {
                return o1.getPeriod().getIniDate().compareTo(o2.getPeriod().getIniDate());
            }
        });

        return treatmentUnits;
    }
}
