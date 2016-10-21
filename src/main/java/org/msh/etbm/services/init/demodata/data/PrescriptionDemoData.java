package org.msh.etbm.services.init.demodata.data;

import org.msh.etbm.commons.date.Period;

/**
 * Stores data about Prescriptions created as demonstration data
 * Created by Mauricio on 19/10/2016.
 */
public class PrescriptionDemoData {

    /**
     * custom medicine id so the regimen will be related to this case
     */
    private String customMedicineId;

    private int doseUnit;

    private int frequency;

    private Period period;

    public String getCustomMedicineId() {
        return customMedicineId;
    }

    public void setCustomMedicineId(String customMedicineId) {
        this.customMedicineId = customMedicineId;
    }

    public int getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(int doseUnit) {
        this.doseUnit = doseUnit;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
