package org.msh.etbm.entities;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.entities.enums.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name="workspace")
public class Workspace implements Serializable, Transactional {

	@Id
	private UUID id;

	@PropertyLog(messageKey="form.name")
	private String name;

	@OneToMany(mappedBy="workspace", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@PropertyLog(ignore=true)
	private List<UserWorkspace> users = new ArrayList<UserWorkspace>();

	@Column(length=150)
	private String description;

	@Column(length=10)
	private String defaultLocale;
	

	@Column(length=200)
	private String defaultTimeZone;

	// frequency of doses in a weekly basis
	private Integer weekFreq1;
	private Integer weekFreq2;
	private Integer weekFreq3;
	private Integer weekFreq4;
	private Integer weekFreq5;
	private Integer weekFreq6;
	private Integer weekFreq7;

	@Column(length=10)
	private String extension;

	@OneToOne(cascade={CascadeType.REMOVE}, fetch= FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private WorkspaceView view;

	/**
	 * Setup the patient name composition to be used in data entry forms and displaying
	 */
	private NameComposition patientNameComposition;
	
	/**
	 * Configuration of the case validation for TB cases
	 */
	private CaseValidationOption caseValidationTB;
	
	/**
	 * Configuration of the case validation for DR-TB cases
	 */
	private CaseValidationOption caseValidationDRTB;
	
	/**
	 * Configuration of the case validation for MNT cases
	 */
	private CaseValidationOption caseValidationNTM;

	/**
	 * If true, the ULA will be displayed once to the user to be accepted
	 */
	private boolean ulaActive;
	
	/**
	 * Setup the case number to be displayed for the suspect cases
	 */
	private DisplayCaseNumber suspectCaseNumber;
	
	/**
	 * Setup the case number to be displayed for the confirmed cases
	 */
	private DisplayCaseNumber confirmedCaseNumber;
	
	/**
	 * Required levels of administrative unit for patient address
	 */
	private Integer patientAddrRequiredLevels;
	
	/**
	 * Indicate if system will send e-mail messages to the users in certain system events (like new orders, orders authorized, etc)
	 */
	private boolean sendSystemMessages;
	
	/**
	 * Setup the quantity of months that the system will consider when it has to alert the user about medicines that will expire.
	 */
	private Integer monthsToAlertExpiredMedicines;
	
	/**
	 * Minimum stock on-hand in months allowed in the unit. If a medicine is equals or under this level,
	 * the system will alert about that 
	 */
	private Integer minStockOnHand;
	
	/**
	 * Maximum stock on-hand in months allowed in the unit. If a medicine is equals or over this level,
	 * the system will alert about that 
	 */
	private Integer maxStockOnHand;
	
	/**
	 * If true, in the medicine in-take monitoring of the case, user will specify if administered the treatment
	 * in DOTS or if it was self-administered by the patient. If false, the user will just select the day patient 
	 * received medicine
	 */
	private TreatMonitoringInput treatMonitoringInput;

    /**
     * If true, allows diagnosis date to be informed after the treatment start
     */
    private boolean allowDiagAfterTreatment;

    /**
     * If true, allows registration date to be informed after the diagnosis date
     */
    private boolean allowRegAfterDiagnosis;

	/**
	 * Setup the adjustment type that represents expired medicine movements
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="expiredMedicineAdjustmentType_ID")
	@PropertyLog(messageKey="Workspace.ExpiredMedAdjustType")
	private FieldValue expiredMedicineAdjustmentType;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="lastTransaction_ID")
	@PropertyLog(ignore=true)
	private TransactionLog lastTransaction;



/*
	public WeeklyFrequency[] getWeeklyFrequencies() {
		WeeklyFrequency[] lst = new WeeklyFrequency[7];

		lst[0] = new WeeklyFrequency(weekFreq1);
		lst[1] = new WeeklyFrequency(weekFreq2);
		lst[2] = new WeeklyFrequency(weekFreq3);
		lst[3] = new WeeklyFrequency(weekFreq4);
		lst[4] = new WeeklyFrequency(weekFreq5);
		lst[5] = new WeeklyFrequency(weekFreq6);
		lst[6] = new WeeklyFrequency(weekFreq7);

		return lst;
	}
*/

/*
	public void setWeeklyFrequency(WeeklyFrequency[] vals) {
		weekFreq1 = vals[0].getValue();
		weekFreq2 = vals[1].getValue();
		weekFreq3 = vals[2].getValue();
		weekFreq4 = vals[3].getValue();
		weekFreq5 = vals[4].getValue();
		weekFreq6 = vals[5].getValue();
		weekFreq7 = vals[6].getValue();
	}
	
	public WeeklyFrequency getWeeklyFrequency(int frequency) {
		switch (frequency) {
		case 1: return new WeeklyFrequency(weekFreq1);
		case 2: return new WeeklyFrequency(weekFreq2);
		case 3: return new WeeklyFrequency(weekFreq3);
		case 4: return new WeeklyFrequency(weekFreq4);
		case 5: return new WeeklyFrequency(weekFreq5);
		case 6: return new WeeklyFrequency(weekFreq6);
		case 7: return new WeeklyFrequency(weekFreq7);
		}
		return null;
	}
*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * Return the case validation setting of the given case classification
	 * @param classification is the classification of the case
	 * @return instance of {@link CaseValidationOption}
	 */
	public CaseValidationOption getCaseValidationOption(CaseClassification classification) {
		switch (classification) {
		case TB:
			return caseValidationTB;
		case DRTB:
			return caseValidationDRTB;
		case NTM:
			return caseValidationNTM;
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
/*
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof Workspace))
			return false;

		return ((Workspace)obj).getId().equals(getId());
	}
*/

	
/*
	@Override
	public String toString() {
		return getName().toString();
	}
*/

/*
	public String getViewUri() {
		WorkspaceView view = getView();
		if ((view == null) || (view.getPictureURI() == null))
			 return "/public/images/globe.png";
		else return view.getPictureURI();
	}
*/

	/**
	 * Returns the name of the language according to the locale
//	 * @param s
	 * @return
	 */
/*
	protected String getLocaleDisplayName(String s) {
		if ((s == null) || (s.isEmpty()))
			return null;

		int p = s.indexOf("_");
		Locale loc = null;
		if (p != -1) {
			String lan = s.substring(0, p);
			String lc = s.substring(p+1);
			loc = new Locale(lan, lc);
		}
		else  loc = new Locale(s);

		return loc.getDisplayName(LocaleSelector.instance().getLocale());
	}
*/

/*
	public String getDefaultDisplayLocale() {
		String s = getDefaultLocale();
		return getLocaleDisplayName(s);
	}
*/


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public List<UserWorkspace> getUsers() {
		return users;
	}

	public void setUsers(List<UserWorkspace> users) {
		this.users = users;
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String getDefaultTimeZone() {
		return defaultTimeZone;
	}

	public void setDefaultTimeZone(String defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}

	/**
	 * Returns the root path of the custom pages for the country. Ex: /brazil or /ukraine 
	 * @return
	 */
/*	public String getRootPath() {
		return ((customPath == null)||(customPath.isEmpty()) ? "/custom/generic": customPath);
	}
*/

	/**
	 * @return the patientNameComposition
	 */
	public NameComposition getPatientNameComposition() {
		return patientNameComposition;
	}

	/**
	 * @param patientNameComposition the patientNameComposition to set
	 */
	public void setPatientNameComposition(NameComposition patientNameComposition) {
		this.patientNameComposition = patientNameComposition;
	}

	/**
	 * @return the patientAddrRequiredLevels
	 */
	public Integer getPatientAddrRequiredLevels() {
		return patientAddrRequiredLevels;
	}

	/**
	 * @param patientAddrRequiredLevels the patientAddrRequiredLevels to set
	 */
	public void setPatientAddrRequiredLevels(Integer patientAddrRequiredLevels) {
		this.patientAddrRequiredLevels = patientAddrRequiredLevels;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the view
	 */
	public WorkspaceView getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(WorkspaceView view) {
		this.view = view;
	}

	/**
	 * @return the sendSystemMessages
	 */
	public boolean isSendSystemMessages() {
		return sendSystemMessages;
	}

	/**
	 * @param sendSystemMessages the sendSystemMessages to set
	 */
	public void setSendSystemMessages(boolean sendSystemMessages) {
		this.sendSystemMessages = sendSystemMessages;
	}

	/**
	 * @return the monthsToAlertExpiredMedicines
	 */
	public Integer getMonthsToAlertExpiredMedicines() {
		return monthsToAlertExpiredMedicines;
	}

	/**
	 * @param monthsToAlertExpiredMedicines the monthsToAlertExpiredMedicines to set
	 */
	public void setMonthsToAlertExpiredMedicines(
			Integer monthsToAlertExpiredMedicines) {
		this.monthsToAlertExpiredMedicines = monthsToAlertExpiredMedicines;
	}


	/* (non-Javadoc)
	 * @see org.msh.tb.entities.Transactional#getLastTransaction()
	 */
	@Override
	public TransactionLog getLastTransaction() {
		return lastTransaction;
	}

	/* (non-Javadoc)
	 * @see org.msh.tb.entities.Transactional#setLastTransaction(org.msh.tb.entities.TransactionLog)
	 */
	@Override
	public void setLastTransaction(TransactionLog transactionLog) {
		this.lastTransaction = transactionLog;
	}

	public boolean isUlaActive() {
		return ulaActive;
	}

	public void setUlaActive(boolean ulaActive) {
		this.ulaActive = ulaActive;
	}

	/**
	 * @return the expiredMedicineAdjustmentType
	 */
	public FieldValue getExpiredMedicineAdjustmentType() {
		return expiredMedicineAdjustmentType;
	}

	/**
	 * @param expiredMedicineAdjustmentType the expiredMedicineAdjustmentType to set
	 */
	public void setExpiredMedicineAdjustmentType(
			FieldValue expiredMedicineAdjustmentType) {
		this.expiredMedicineAdjustmentType = expiredMedicineAdjustmentType;
	}

	/**
	 * @return the caseValidationTB
	 */
	public CaseValidationOption getCaseValidationTB() {
		return caseValidationTB;
	}

	/**
	 * @param caseValidationTB the caseValidationTB to set
	 */
	public void setCaseValidationTB(CaseValidationOption caseValidationTB) {
		this.caseValidationTB = caseValidationTB;
	}

	/**
	 * @return the caseValidationDRTB
	 */
	public CaseValidationOption getCaseValidationDRTB() {
		return caseValidationDRTB;
	}

	/**
	 * @param caseValidationDRTB the caseValidationDRTB to set
	 */
	public void setCaseValidationDRTB(CaseValidationOption caseValidationDRTB) {
		this.caseValidationDRTB = caseValidationDRTB;
	}

	/**
	 * @return the caseValidationNTM
	 */
	public CaseValidationOption getCaseValidationNTM() {
		return caseValidationNTM;
	}

	/**
	 * @param caseValidationNTM the caseValidationNTM to set
	 */
	public void setCaseValidationNTM(CaseValidationOption caseValidationNTM) {
		this.caseValidationNTM = caseValidationNTM;
	}

	/**
	 * @return the minStockOnHand
	 */
	public Integer getMinStockOnHand() {
		return minStockOnHand;
	}

	/**
	 * @param minStockOnHand the minStockOnHand to set
	 */
	public void setMinStockOnHand(Integer minStockOnHand) {
		this.minStockOnHand = minStockOnHand;
	}

	/**
	 * @return the maxStockOnHand
	 */
	public Integer getMaxStockOnHand() {
		return maxStockOnHand;
	}

	/**
	 * @param maxStockOnHand the maxStockOnHand to set
	 */
	public void setMaxStockOnHand(Integer maxStockOnHand) {
		this.maxStockOnHand = maxStockOnHand;
	}

	/**
	 * @return the suspectCaseNumber
	 */
	public DisplayCaseNumber getSuspectCaseNumber() {
		return suspectCaseNumber;
	}

	/**
	 * @param suspectCaseNumber the suspectCaseNumber to set
	 */
	public void setSuspectCaseNumber(DisplayCaseNumber suspectCaseNumber) {
		this.suspectCaseNumber = suspectCaseNumber;
	}

	/**
	 * @return the confirmedCaseNumber
	 */
	public DisplayCaseNumber getConfirmedCaseNumber() {
		return confirmedCaseNumber;
	}

	/**
	 * @param confirmedCaseNumber the confirmedCaseNumber to set
	 */
	public void setConfirmedCaseNumber(DisplayCaseNumber confirmedCaseNumber) {
		this.confirmedCaseNumber = confirmedCaseNumber;
	}

	/**
	 * @return the treatMonitoringInput
	 */
	public TreatMonitoringInput getTreatMonitoringInput() {
		return treatMonitoringInput;
	}

	/**
	 * @param treatMonitoringInput the treatMonitoringInput to set
	 */
	public void setTreatMonitoringInput(TreatMonitoringInput treatMonitoringInput) {
		this.treatMonitoringInput = treatMonitoringInput;
	}

    public boolean isAllowDiagAfterTreatment() {
        return allowDiagAfterTreatment;
    }

    public void setAllowDiagAfterTreatment(boolean allowDiagAfterTreatment) {
        this.allowDiagAfterTreatment = allowDiagAfterTreatment;
    }

    public boolean isAllowRegAfterDiagnosis() {
        return allowRegAfterDiagnosis;
    }

    public void setAllowRegAfterDiagnosis(boolean allowRegAfterDiagnosis) {
        this.allowRegAfterDiagnosis = allowRegAfterDiagnosis;
    }

	public Integer getWeekFreq1() {
		return weekFreq1;
	}

	public void setWeekFreq1(Integer weekFreq1) {
		this.weekFreq1 = weekFreq1;
	}

	public Integer getWeekFreq2() {
		return weekFreq2;
	}

	public void setWeekFreq2(Integer weekFreq2) {
		this.weekFreq2 = weekFreq2;
	}

	public Integer getWeekFreq3() {
		return weekFreq3;
	}

	public void setWeekFreq3(Integer weekFreq3) {
		this.weekFreq3 = weekFreq3;
	}

	public Integer getWeekFreq4() {
		return weekFreq4;
	}

	public void setWeekFreq4(Integer weekFreq4) {
		this.weekFreq4 = weekFreq4;
	}

	public Integer getWeekFreq5() {
		return weekFreq5;
	}

	public void setWeekFreq5(Integer weekFreq5) {
		this.weekFreq5 = weekFreq5;
	}

	public Integer getWeekFreq6() {
		return weekFreq6;
	}

	public void setWeekFreq6(Integer weekFreq6) {
		this.weekFreq6 = weekFreq6;
	}

	public Integer getWeekFreq7() {
		return weekFreq7;
	}

	public void setWeekFreq7(Integer weekFreq7) {
		this.weekFreq7 = weekFreq7;
	}
}
