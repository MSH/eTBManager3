package org.msh.etbm.db.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * TB units are health facilities that handle patients, store commodities (medicines)
 * or participate on the TB management flow
 */
@Entity
@DiscriminatorValue("unit")
public class Tbunit extends Unit {

    /**
     * TB cases handled by the health unit (susceptible TB, DR-TB and NTM)
     */
    private boolean tbUnit;
    private boolean mdrUnit;
    private boolean ntmUnit;

    /**
     * Indicate if this is a notification health unit
     */
    private boolean notificationUnit;

    /**
     * if true, unit can register medicine dispensing to patient
     */
    private boolean patientDispensing;

    /**
     * Number of days to estimate medicine orders
     */
    private Integer numDaysOrder;



    
    @Override
    public String toString() {
    	return getName().toString();
    }

	public Integer getNumDaysOrder() {
		return numDaysOrder;
	}

	public void setNumDaysOrder(Integer numDaysOrder) {
		this.numDaysOrder = numDaysOrder;
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

    @Override
    public String getTypeName() {
        return "tbunit";
    }

    public boolean isTbUnit() {
        return tbUnit;
    }

    public void setTbUnit(boolean tbUnit) {
        this.tbUnit = tbUnit;
    }

    public boolean isMdrUnit() {
        return mdrUnit;
    }

    public void setMdrUnit(boolean mdrUnit) {
        this.mdrUnit = mdrUnit;
    }

    public boolean isNtmUnit() {
        return ntmUnit;
    }

    public void setNtmUnit(boolean ntmUnit) {
        this.ntmUnit = ntmUnit;
    }

    public boolean isNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }
}
