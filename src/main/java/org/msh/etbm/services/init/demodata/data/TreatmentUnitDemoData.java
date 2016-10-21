package org.msh.etbm.services.init.demodata.data;

import org.msh.etbm.commons.date.Period;

/**
 * Stores data about TreatmentUnits created as demonstration data
 * Created by Mauricio on 19/10/2016.
 */
public class TreatmentUnitDemoData {

    private Period period;

    private String unitName;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
