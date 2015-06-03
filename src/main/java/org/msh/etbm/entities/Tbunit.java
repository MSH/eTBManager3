package org.msh.etbm.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.entities.enums.DispensingFrequency;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="tbunit")
public class Tbunit extends WSObject implements Serializable, EntityState {
	private static final long serialVersionUID = 7444534501216755257L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@Embedded
	@PropertyLog(messageKey="form.name", operations={Operation.NEW})
	private LocalizedNameComp name = new LocalizedNameComp();
	
	@Column(length=200)
	private String address;
	
	@Column(length=200)
	private String addressCont;
	
	@Column(length=50)
	private String zipCode;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="FIRSTLINE_SUPPLIER_ID")
	private Tbunit firstLineSupplier;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="SECONDLINE_SUPPLIER_ID")
	private Tbunit secondLineSupplier;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="AUTHORIZERUNIT_ID")
    private Tbunit authorizerUnit;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ADMINUNIT_ID")
	@NotNull
	@PropertyLog(messageKey="UserView.ADMINUNIT", operations={Operation.NEW})
	private AdministrativeUnit adminUnit;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="HEALTHSYSTEM_ID")
	@NotNull
	@PropertyLog(operations={Operation.NEW})
	private HealthSystem healthSystem;
	
	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String legacyId;
	
	@Column(length=100)
	@PropertyLog(messageKey="TbCase.phoneNumber")
	private String phoneNumber;

	// ready to be removed from the system
    private boolean batchControl;
    
    private boolean treatmentHealthUnit;
    private boolean medicineStorage;
    private boolean changeEstimatedQuantity;
    private boolean receivingFromSource;
    private boolean medicineSupplier;
    private DispensingFrequency dispensingFrequency;
    
    private boolean tbHealthUnit;
    private boolean mdrHealthUnit;
    private boolean notifHealthUnit;
    private boolean patientDispensing;
    private boolean ntmHealthUnit;

    @PropertyLog(messageKey="EntityState.ACTIVE")
    private boolean active;

    /**
	 * Check if the medicine should be included in the order if it doesn't reach the minimun quantity
	 */
	private boolean orderOverMinimum;
    
    private Integer numDaysOrder;

    /**
     * Date when this TB unit started the medicine management 
     */
    @Temporal(TemporalType.DATE)
    private Date medManStartDate;

    /**
     * Limit date to create movements for this unit. The movement has to equals or after this date. 
     */
    @Temporal(TemporalType.DATE)
    private Date limitDateMedicineMovement;
    
    /**
     * Order address
     */
    @Column(length=200)
    private String shipAddress;

    /**
     * Order address (continuing)
     */
    @Column(length=200)
    private String shipAddressCont;

    /**
     * Order contact name
     */
    @Column(length=200)
    private String shipContactName;
    
    /**
     * Order contact phone
     */
    @Column(length=200)
    private String shipContactPhone;

    @Column(length=100)
    private String shipZipCode;
    
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="SHIP_ADMINUNIT_ID")
    private AdministrativeUnit shipAdminUnit;
    
    
    /**
     * Check if medicine management was already started for this TB Unit
     * @return
     */
    public boolean isMedicineManagementStarted() {
    	return medManStartDate != null;
    }

    
    @Override
    public String toString() {
    	return getName().toString();
    }
    
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Tbunit))
			return false;
		
		return ((Tbunit)other).getId().equals(id);
	}
	
	public boolean isOrderMedicines() {
		return (firstLineSupplier != null) && (secondLineSupplier != null);
	}
	
	public Integer getNumDaysOrder() {
		return numDaysOrder;
	}

	public void setNumDaysOrder(Integer numDaysOrder) {
		this.numDaysOrder = numDaysOrder;
	}

	public boolean isReceivingFromSource() {
		return receivingFromSource;
	}

	public void setReceivingFromSource(boolean receivingFromSource) {
		this.receivingFromSource = receivingFromSource;
	}

	public boolean isBatchControl() {
		return batchControl;
	}

	public void setBatchControl(boolean batchControl) {
		this.batchControl = batchControl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isChangeEstimatedQuantity() {
		return changeEstimatedQuantity;
	}

	public void setChangeEstimatedQuantity(boolean changeEstimatedQuantity) {
		this.changeEstimatedQuantity = changeEstimatedQuantity;
	}

	public boolean isTreatmentHealthUnit() {
		return treatmentHealthUnit;
	}

	public void setTreatmentHealthUnit(boolean treatmentHealthUnit) {
		this.treatmentHealthUnit = treatmentHealthUnit;
	}

	public boolean isMedicineStorage() {
		return medicineStorage;
	}

	public void setMedicineStorage(boolean medicineStorage) {
		this.medicineStorage = medicineStorage;
	}

	public boolean isMedicineSupplier() {
		return medicineSupplier;
	}

	public void setMedicineSupplier(boolean medicineSupplier) {
		this.medicineSupplier = medicineSupplier;
	}

	public Tbunit getFirstLineSupplier() {
		return firstLineSupplier;
	}

	public void setFirstLineSupplier(Tbunit firstLineSupplier) {
		this.firstLineSupplier = firstLineSupplier;
	}

	public Tbunit getSecondLineSupplier() {
		return secondLineSupplier;
	}

	public void setSecondLineSupplier(Tbunit secondLineSupplier) {
		this.secondLineSupplier = secondLineSupplier;
	}

	public Tbunit getAuthorizerUnit() {
		return authorizerUnit;
	}

	public void setAuthorizerUnit(Tbunit authorizerUnit) {
		this.authorizerUnit = authorizerUnit;
	}

	public DispensingFrequency getDispensingFrequency() {
		return dispensingFrequency;
	}

	public void setDispensingFrequency(DispensingFrequency dispensingFrequency) {
		this.dispensingFrequency = dispensingFrequency;
	}

	public LocalizedNameComp getName() {
		return name;
	}

	public void setName(LocalizedNameComp name) {
		this.name = name;
	}

	public boolean isOrderOverMinimum() {
		return orderOverMinimum;
	}

	public void setOrderOverMinimum(boolean orderOverMinimum) {
		this.orderOverMinimum = orderOverMinimum;
	}

	public String getLegacyId() {
		return legacyId;
	}

	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}
	

	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * @param adminUnit the adminUnit to set
	 */
	public void setAdminUnit(AdministrativeUnit adminUnit) {
		this.adminUnit = adminUnit;
	}

	/**
	 * @return the adminUnit
	 */
	public AdministrativeUnit getAdminUnit() {
		return adminUnit;
	}

	/**
	 * @return the tbHealthUnit
	 */
	public boolean isTbHealthUnit() {
		return tbHealthUnit;
	}

	/**
	 * @param tbHealthUnit the tbHealthUnit to set
	 */
	public void setTbHealthUnit(boolean tbHealthUnit) {
		this.tbHealthUnit = tbHealthUnit;
	}

	/**
	 * @return the mdrHealthUnit
	 */
	public boolean isMdrHealthUnit() {
		return mdrHealthUnit;
	}

	/**
	 * @param mdrHealthUnit the mdrHealthUnit to set
	 */
	public void setMdrHealthUnit(boolean mdrHealthUnit) {
		this.mdrHealthUnit = mdrHealthUnit;
	}

	/**
	 * @param healthSystem the healthSystem to set
	 */
	public void setHealthSystem(HealthSystem healthSystem) {
		this.healthSystem = healthSystem;
	}

	/**
	 * @return the healthSystem
	 */
	public HealthSystem getHealthSystem() {
		return healthSystem;
	}

	/**
	 * @param notifHealthUnit the notifHealthUnit to set
	 */
	public void setNotifHealthUnit(boolean notifHealthUnit) {
		this.notifHealthUnit = notifHealthUnit;
	}

	/**
	 * @return the notifHealthUnit
	 */
	public boolean isNotifHealthUnit() {
		return notifHealthUnit;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean newState) {
		active = newState;
	}

	public Date getMedManStartDate() {
		return medManStartDate;
	}

	public void setMedManStartDate(Date medManStartDate) {
		this.medManStartDate = medManStartDate;
	}


	/**
	 * @return the patientDispensing
	 */
	public boolean isPatientDispensing() {
		return patientDispensing;
	}


	/**
	 * @param patientDispensing the patientDispensing to set
	 */
	public void setPatientDispensing(boolean patientDispensing) {
		this.patientDispensing = patientDispensing;
	}



	/**
	 * @return the shipAddress
	 */
	public String getShipAddress() {
		return shipAddress;
	}


	/**
	 * @param shipAddress the shipAddress to set
	 */
	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}


	/**
	 * @return the shipAddressCont
	 */
	public String getShipAddressCont() {
		return shipAddressCont;
	}


	/**
	 * @param shipAddressCont the shipAddressCont to set
	 */
	public void setShipAddressCont(String shipAddressCont) {
		this.shipAddressCont = shipAddressCont;
	}


	/**
	 * @return the shipContactName
	 */
	public String getShipContactName() {
		return shipContactName;
	}


	/**
	 * @param shipContactName the shipContactName to set
	 */
	public void setShipContactName(String shipContactName) {
		this.shipContactName = shipContactName;
	}


	/**
	 * @return the shipContactPhone
	 */
	public String getShipContactPhone() {
		return shipContactPhone;
	}


	/**
	 * @param shipContactPhone the shipContactPhone to set
	 */
	public void setShipContactPhone(String shipContactPhone) {
		this.shipContactPhone = shipContactPhone;
	}


	/**
	 * @return the addressCont
	 */
	public String getAddressCont() {
		return addressCont;
	}


	/**
	 * @param addressCont the addressCont to set
	 */
	public void setAddressCont(String addressCont) {
		this.addressCont = addressCont;
	}


	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}


	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public boolean isNtmHealthUnit() {
		return ntmHealthUnit;
	}
	
	public void setNtmHealthUnit(boolean ntmHealthUnit) {
		this.ntmHealthUnit = ntmHealthUnit;
	}

	/**
	 * @return the limitDateMedicineMovement
	 */
	public Date getLimitDateMedicineMovement() {
		return limitDateMedicineMovement;
	}
	
	/**
	 * @param limitDateMedicineMovement the limitDateMedicineMovement to set
	 */
	public void setLimitDateMedicineMovement(Date limitDateMedicineMovement) {
		this.limitDateMedicineMovement = limitDateMedicineMovement;
	}


	/**
	 * @return the shipZipCode
	 */
	public String getShipZipCode() {
		return shipZipCode;
	}


	/**
	 * @param shipZipCode the shipZipCode to set
	 */
	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}


	/**
	 * @return the shipAdminUnit
	 */
	public AdministrativeUnit getShipAdminUnit() {
		return shipAdminUnit;
	}


	/**
	 * @param shipAdminUnit the shipAdminUnit to set
	 */
	public void setShipAdminUnit(AdministrativeUnit shipAdminUnit) {
		this.shipAdminUnit = shipAdminUnit;
	}
	
}
