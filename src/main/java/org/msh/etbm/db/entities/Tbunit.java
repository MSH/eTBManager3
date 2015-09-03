package org.msh.etbm.db.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("unit")
public class Tbunit extends Unit {

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="FIRSTLINE_SUPPLIER_ID")
	private Tbunit firstLineSupplier;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="SECONDLINE_SUPPLIER_ID")
	private Tbunit secondLineSupplier;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="AUTHORIZERUNIT_ID")
    private Tbunit authorizerUnit;

    private boolean treatmentHealthUnit;
    private boolean medicineStorage;
    private boolean receivingFromSource;
    private boolean medicineSupplier;

    private boolean tbHealthUnit;
    private boolean mdrHealthUnit;
    private boolean notifHealthUnit;
    private boolean patientDispensing;
    private boolean ntmHealthUnit;

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
}
