package org.msh.etbm.services.init.demodata.data;

import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.db.entities.Regimen;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Stores data about MedicineRegimen created as demonstration data
 * Created by Mauricio on 19/10/2016.
 */
public class MedicineRegimenDemoData {

    /**
     * custom medicine id so the regimen will be related to this case
     */
    private String medicineCustomId;

    private int defaultDoseUnit;

    private int defaultFrequency;

    private int iniDay;

    private int days;

    public String getMedicineCustomId() {
        return medicineCustomId;
    }

    public void setMedicineCustomId(String medicineCustomId) {
        this.medicineCustomId = medicineCustomId;
    }

    public int getDefaultDoseUnit() {
        return defaultDoseUnit;
    }

    public void setDefaultDoseUnit(int defaultDoseUnit) {
        this.defaultDoseUnit = defaultDoseUnit;
    }

    public int getDefaultFrequency() {
        return defaultFrequency;
    }

    public void setDefaultFrequency(int defaultFrequency) {
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
