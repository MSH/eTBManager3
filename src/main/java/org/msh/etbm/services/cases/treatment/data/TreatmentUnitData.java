package org.msh.etbm.services.cases.treatment.data;

import org.msh.etbm.services.admin.units.data.UnitData;

import java.util.Date;

/**
 * Created by rmemoria on 23/5/16.
 */
public class TreatmentUnitData {
    private UnitData unit;
    private Date ini;
    private Date end;

    public UnitData getUnit() {
        return unit;
    }

    public void setUnit(UnitData unit) {
        this.unit = unit;
    }

    public Date getIni() {
        return ini;
    }

    public void setIni(Date ini) {
        this.ini = ini;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
