package org.msh.etbm.db.entities;

import org.msh.etbm.services.admin.units.UnitType;

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
    private boolean tbFacility;
    private boolean drtbFacility;
    private boolean ntmFacility;

    /**
     * Indicate if this is a notification health unit
     */
    private boolean notificationUnit;

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


    @Override
    public UnitType getType() {
        return UnitType.TBUNIT;
    }

    public boolean isTbFacility() {
        return tbFacility;
    }

    public void setTbFacility(boolean tbFacility) {
        this.tbFacility = tbFacility;
    }

    public boolean isNtmFacility() {
        return ntmFacility;
    }

    public void setNtmFacility(boolean ntmFacility) {
        this.ntmFacility = ntmFacility;
    }

    public boolean isNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public boolean isDrtbFacility() {
        return drtbFacility;
    }

    public void setDrtbFacility(boolean drtbFacility) {
        this.drtbFacility = drtbFacility;
    }
}
