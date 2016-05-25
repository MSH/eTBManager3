package org.msh.etbm.services.cases.treatment;

import org.msh.etbm.commons.date.Period;
import org.msh.etbm.services.admin.units.data.UnitData;

/**
 * Created by rmemoria on 23/5/16.
 */
public class TreatmentUnitData {
    private UnitData unit;
    private Period period;

    public UnitData getUnit() {
        return unit;
    }

    public void setUnit(UnitData unit) {
        this.unit = unit;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
