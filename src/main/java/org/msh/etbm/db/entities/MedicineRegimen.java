package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "medicineregimen")
public class MedicineRegimen extends Synchronizable {


    @ManyToOne
    @JoinColumn(name = "MEDICINE_ID")
    @NotNull
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "REGIMEN_ID")
    private Regimen regimen;

    @NotNull
    private int defaultDoseUnit;

    @NotNull
    private int defaultFrequency;

    /**
     * The initial day of the treatment for this medicine in the regimen
     */
    private int iniDay;

    /**
     * Number of days of treatment for this medicine
     */
    private int days;


    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
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

    public Regimen getRegimen() {
        return regimen;
    }

    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }
}
