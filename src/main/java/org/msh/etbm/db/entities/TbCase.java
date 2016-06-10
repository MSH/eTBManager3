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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Store information about a case (TB or DR-TB)
 * @author Ricardo Memoria
 *
 */
@Entity
@Inheritance(strategy =  InheritanceType.JOINED)
@Table(name = "tbcase")
public class TbCase extends WorkspaceEntity {

	@Version
	@PropertyLog(ignore = true)
	private Integer version;

	@PropertyLog(messageKey = "CaseClassification")
	private CaseClassification suspectClassification;

    /**
     * The code of the patient at the moment of registration (presumptive code)
     */
	@Column(length = 50)
	@PropertyLog(operations = {Operation.NEW, Operation.DELETE})
	private String registrationCode;

    private String caseCode;

	private Integer daysTreatPlanned;
	
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
	
	@Temporal(TemporalType.DATE)
	private Date endIntensivePhase;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGIMEN_ID")
	private Regimen regimen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGIMEN_INI_ID")
	private Regimen regimenIni;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_UNIT_ID")
	@NotNull
	private Tbunit ownerUnit ;
	
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase")
	@PropertyLog(ignore = true)
	private List<TreatmentHealthUnit> healthUnits = new ArrayList<>();

	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase")
	@PropertyLog(ignore = true)
	private List<PrescribedMedicine> prescribedMedicines = new ArrayList<>();

	@NotNull
	private CaseState state;
	
	@NotNull
	private ValidationState validationState;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String registrationGroup;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE}, messageKey = "TbCase.previouslyTreatedType")
    private PatientType previouslyTreatedType;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private CaseDefinition caseDefinition;

	@PropertyLog(operations = {Operation.NEW, Operation.DELETE})
	private DiagnosisType diagnosisType;
	
	@PropertyLog(operations = {Operation.NEW, Operation.DELETE})
	private DrugResistanceType drugResistanceType;

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

	private Nationality nationality;
	
	@Column(length = 100)
	private String otherOutcome;
	
	@Column(length = 50)
	@PropertyLog(messageKey = "form.customId")
	private String customId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NOTIFICATION_UNIT_ID")
	@PropertyLog(operations = {Operation.NEW, Operation.DELETE})
	private Tbunit notificationUnit;
	
	private boolean notifAddressChanged;

    private boolean tbContact;

    private boolean movedSecondLineTreatment;
	
	@Column(length = 100)
	private String patientContactName;
	
	@Lob
    @PropertyLog(messageKey = "global.comments")
	private String comments;
	
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

    @Column(name = "CURR_LOCALITYTYPE")
    private LocalityType currLocalityType;

    @Column(length = 50)
	private String phoneNumber;
	
	@Column(length = 50)
    @PropertyLog(messageKey = "global.mobile")
	private String mobileNumber;
	
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
	private List<CaseSideEffect> sideEffects = new ArrayList<>();

	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
	private List<CaseComorbidity> comorbidities = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
	@OrderBy("date desc")
    @PropertyLog(ignore = true)
	private List<MedicalExamination> examinations = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
	private List<ExamXRay> resXRay = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(messageKey = "cases.contacts")
	private List<TbContact> contacts = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "tbcase", fetch = FetchType.LAZY)
    @PropertyLog(ignore = true)
	private List<TreatmentMonitoring> treatmentMonitoring = new ArrayList<>();
	

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

    @PropertyLog(ignore = true)
	private int issueCounter;

    @Temporal(TemporalType.DATE)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private Date lastBmuDateTbRegister;

    @Column(length = 50)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String lastBmuTbRegistNumber;
	
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
	 * Check if the regimen has changed compared to the initial regimen
	 * @return
	 */
	public boolean isInitialRegimenChanged() {
		if (regimen == regimenIni) {
            return false;
        }

		if ((regimen == null) || (regimenIni == null)) {
            return true;
        }

		return regimen.getId().equals(regimenIni.getId());
	}


	/**
	 * Return number of month of treatment based on the date 
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
     * Return list of treatment health units sorted by period
     * @return
     */
    public List<TreatmentHealthUnit> getSortedTreatmentHealthUnits() {
        // sort the periods
        Collections.sort(healthUnits, (o1, o2) -> o1.getPeriod().getIniDate().compareTo(o2.getPeriod().getIniDate()));

        return healthUnits;
    }

	
	/**
	 * Return list of prescribed medicines sorted by medicine name and initial date of the period
	 * @return
	 */
	public List<PrescribedMedicine> getSortedPrescribedMedicines() {
		// sort the periods
		Collections.sort(prescribedMedicines, (pm1, pm2) -> {
            int val = pm1.getProduct().getShortName().compareTo(pm2.getProduct().getShortName());
            return val != 0 ? val : pm1.getPeriod().getIniDate().compareTo(pm2.getPeriod().getEndDate());
        });
		
		return prescribedMedicines;
	}
	

	/**
	 * Returns if the case is validated or not
	 * @return true - if the case is validated, false - otherwise
	 */
	public boolean isValidated() {
		return getValidationState() == ValidationState.VALIDATED;
	}
	
	/**
	 * Returns if the case is a pulmonary TB/MDRTB
	 * @return - true if it is pulmonary or pulmonary/extrapulmonary
	 */
	public boolean isPulmonary() {
		return (getInfectionSite() != null) && ((infectionSite == InfectionSite.PULMONARY) || (infectionSite == InfectionSite.BOTH));
	}

	
	/**
	 * Returns if the case is a extrapulmonary TB/MDRTB
	 * @return - true if it is extrapulmonary or pulmonary/extrapulmonary
	 */
	public boolean isExtrapulmonary() {
		return (getInfectionSite() != null) && ((infectionSite == InfectionSite.EXTRAPULMONARY) || (infectionSite == InfectionSite.BOTH));
	}
	
	/**
	 * Search for side effect data by the side effect
	 * @param sideEffect - FieldValue object representing the side effect
	 * @return - CaseSideEffect instance containing side effect data of the case, or null if there is no side effect data
	 */
	public CaseSideEffect findSideEffectData(String sideEffect) {
		for (CaseSideEffect se: getSideEffects()) {
			if (se.getSideEffect().getValue().equals(sideEffect)) {
                return se;
            }
		}
		return null;
	}

	
	/**
	 * Return the unit that transferred the case in
	 * @return instance of {@link TreatmentHealthUnit} containing information about the transfer out 
	 */
	public TreatmentHealthUnit getTransferInUnit() {
		return (state == CaseState.TRANSFERRING) && (healthUnits.size() > 1) ? healthUnits.get(healthUnits.size() - 1) : null;
	}
	
	
	/**
	 * Return the unit that transferred the case out
	 * @return instance of {@link TreatmentHealthUnit} containing information about the transfer in 
	 */
	public TreatmentHealthUnit getTransferOutUnit() {
		return (state == CaseState.TRANSFERRING) && (healthUnits.size() > 1) ? healthUnits.get(healthUnits.size() - 2) : null;
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
	 * @param patientNumber - patient record number
	 * @param caseNumber - case number associated to the patient
	 * @return - formated case number
	 */
	static public String formatCaseNumber(int patientNumber, int caseNumber) {
		DecimalFormat df = new DecimalFormat("000");
		String s = df.format(patientNumber);

        return caseNumber > 1 ? s + "-" + Integer.toString(caseNumber) : s;
	}

	
	/**
	 * Check if the case is open
	 * @return
	 */
	public boolean isOpen() {
		return state != null ? state.ordinal() <=  CaseState.TRANSFERRING.ordinal() : false;
	}


	/**
	 * Is case on treatment ?
	 * @return
	 */
	public boolean isOnTreatment() {
		return (state == CaseState.ONTREATMENT) || (state == CaseState.TRANSFERRING);
	}

	/**
	 * Return the treatment period of the intensive phase
	 * @return
	 */
	public Period getIntensivePhasePeriod() {
		if ((treatmentPeriod == null) || (treatmentPeriod.isEmpty())) {
            return null;
        }

        return endIntensivePhase != null ?
                new Period(treatmentPeriod.getIniDate(), endIntensivePhase) :
                new Period(treatmentPeriod);
	}

	/**
	 * Returns patient age at the date of the notification
	 * @return
	 */
	public int getPatientAge() {
		if (age != null) {
            return age;
        }

		Patient p = getPatient();
		if (p == null) {
            return 0;
        }

		Date dt = p.getBirthDate();
		Date dt2 = diagnosisDate;
		
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

	public List<TreatmentHealthUnit> getHealthUnits() {
		return healthUnits;
	}

	public void setHealthUnits(List<TreatmentHealthUnit> healthUnits) {
		this.healthUnits = healthUnits;
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


	public boolean isNotifAddressChanged() {
		return notifAddressChanged;
	}


	public void setNotifAddressChanged(boolean notifAddressChanged) {
		this.notifAddressChanged = notifAddressChanged;
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

    public Nationality getNationality() {
		return nationality;
	}


	public void setNationality(Nationality nationality) {
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
	 * @return the comorbidities
	 */
	public List<CaseComorbidity> getComorbidities() {
		return comorbidities;
	}


	/**
	 * @param comorbidities the comorbidities to set
	 */
	public void setComorbidities(List<CaseComorbidity> comorbidities) {
		this.comorbidities = comorbidities;
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
	public String getRegistrationCode() {
		return registrationCode;
	}


	/**
	 * @param registrationCode the registrationCode to set
	 */
	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}


	/**
	 * @return the drugResistanceType
	 */
	public DrugResistanceType getDrugResistanceType() {
		return drugResistanceType;
	}


	/**
	 * @param drugResistanceType the drugResistanceType to set
	 */
	public void setDrugResistanceType(DrugResistanceType drugResistanceType) {
		this.drugResistanceType = drugResistanceType;
	}


	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}


	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
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
	 * @param tbContact the tbContact to set
	 */
	public void setTbContact(boolean tbContact) {
		this.tbContact = tbContact;
	}


	/**
	 * @return the tbContact
	 */
	public boolean isTbContact() {
		return tbContact;
	}


	/**
	 * @return the contacts
	 */
	public List<TbContact> getContacts() {
		return contacts;
	}


	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<TbContact> contacts) {
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
	 * @return the daysTreatPlanned
	 */
	public Integer getDaysTreatPlanned() {
		return daysTreatPlanned;
	}


	/**
	 * @param daysTreatPlanned the daysTreatPlanned to set
	 */
	public void setDaysTreatPlanned(Integer daysTreatPlanned) {
		this.daysTreatPlanned = daysTreatPlanned;
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


	/**
	 * @return the validationState
	 */
	public ValidationState getValidationState() {
		return validationState;
	}


	/**
	 * @param validationState the validationState to set
	 */
	public void setValidationState(ValidationState validationState) {
		this.validationState = validationState;
	}


	/**
	 * @return the issueCounter
	 */
	public int getIssueCounter() {
		return issueCounter;
	}


	/**
	 * @param issueCount the issueCounter to set
	 */
	public void setIssueCounter(int issueCount) {
		this.issueCounter = issueCount;
	}

	public void incIssueCounter() {
		issueCounter++;
	}


	public void setPrescribedMedicines(List<PrescribedMedicine> prescribedMedicines) {
		this.prescribedMedicines = prescribedMedicines;
	}


	public List<PrescribedMedicine> getPrescribedMedicines() {
		return prescribedMedicines;
	}


	public Period getTreatmentPeriod() {
		return treatmentPeriod;
	}


	public void setTreatmentPeriod(Period treatmentPeriod) {
		this.treatmentPeriod = treatmentPeriod;
	}


	public Integer getVersion() {
		return version;
	}


	public void setVersion(Integer version) {
		this.version = version;
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
	 * @return the endIntensivePhase
	 */
	public Date getEndIntensivePhase() {
		return endIntensivePhase;
	}


	/**
	 * @param endIntensivePhase the endIntensivePhase to set
	 */
	public void setEndIntensivePhase(Date endIntensivePhase) {
		this.endIntensivePhase = endIntensivePhase;
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


	/**
	 * @return the regimenIni
	 */
	public Regimen getRegimenIni() {
		return regimenIni;
	}


	/**
	 * @param regimenIni the regimenIni to set
	 */
	public void setRegimenIni(Regimen regimenIni) {
		this.regimenIni = regimenIni;
	}


    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
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

   public PatientType getPreviouslyTreatedType() {
        return previouslyTreatedType;
    }

    public void setPreviouslyTreatedType(PatientType previouslyTreatedType) {
        this.previouslyTreatedType = previouslyTreatedType;
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

    public LocalityType getCurrLocalityType() {
        return currLocalityType;
    }

    public void setCurrLocalityType(LocalityType currLocalityType) {
        this.currLocalityType = currLocalityType;
    }

    @Override
    public String getDisplayString() {
        return "(" + classification + ") " + patient.getDisplayString();
    }
}
