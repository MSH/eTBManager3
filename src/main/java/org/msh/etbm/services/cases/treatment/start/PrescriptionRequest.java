package org.msh.etbm.services.cases.treatment.start;

import java.util.UUID;

/**
 * Created by rmemoria on 13/9/16.
 */
public class PrescriptionRequest {

    private UUID productId;
    private int monthIni;
    private int months;
    private int doseUnit;
    private int frequency;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getMonthIni() {
        return monthIni;
    }

    public void setMonthIni(int monthIni) {
        this.monthIni = monthIni;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
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
}
