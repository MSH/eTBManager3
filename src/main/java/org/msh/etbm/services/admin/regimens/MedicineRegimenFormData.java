package org.msh.etbm.services.admin.regimens;

import java.util.UUID;

/**
 * Created by rmemoria on 6/1/16.
 */
public class MedicineRegimenFormData {
    private UUID medicine;
    private Integer defaultDoseUnit;
    private Integer defaultFrequency;
    private int iniDay;
    private int days;

    public UUID getMedicine() {
        return medicine;
    }

    public void setMedicine(UUID medicine) {
        this.medicine = medicine;
    }

    public Integer getDefaultDoseUnit() {
        return defaultDoseUnit;
    }

    public void setDefaultDoseUnit(Integer defaultDoseUnit) {
        this.defaultDoseUnit = defaultDoseUnit;
    }

    public Integer getDefaultFrequency() {
        return defaultFrequency;
    }

    public void setDefaultFrequency(Integer defaultFrequency) {
        this.defaultFrequency = defaultFrequency;
    }

    public int getIniDay() {
        return iniDay;
    }

    public void setIniDay(int iniDay) {
        this.iniDay = iniDay;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
