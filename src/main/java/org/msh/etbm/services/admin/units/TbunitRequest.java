package org.msh.etbm.services.admin.units;

import org.msh.etbm.db.entities.Unit;

import java.util.UUID;

/**
 * Data about a TB unit request
 *
 * Created by rmemoria on 28/10/15.
 */
public class TbunitRequest extends UnitRequest {

    private boolean tbUnit;
    private boolean mdrUnit;
    private boolean ntmUnit;

    private boolean notificationUnit;

    private boolean patientDispensing;

    private Integer numDaysOrder;


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

    public boolean isPatientDispensing() {
        return patientDispensing;
    }

    public void setPatientDispensing(boolean patientDispensing) {
        this.patientDispensing = patientDispensing;
    }

    public Integer getNumDaysOrder() {
        return numDaysOrder;
    }

    public void setNumDaysOrder(Integer numDaysOrder) {
        this.numDaysOrder = numDaysOrder;
    }

    public boolean isNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }
}
