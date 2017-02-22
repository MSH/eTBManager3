package org.msh.etbm.services.cases.treatment.data;

import java.util.Date;
import java.util.UUID;

/**
 * Period of prescription of a product (medicine) with information about
 * the dosage unit, weekly frequency and user comments
 * <p>
 * Created by rmemoria on 29/5/16.
 */
public class PrescriptionPeriod {
    private UUID prescriptionId;
    private Date ini;
    private Date end;
    private int doseUnit;
    private int frequency;
    private String comments;

    public PrescriptionPeriod(UUID prescriptionId, Date ini, Date end, int doseUnit, int frequency, String comments) {
        super();
        this.prescriptionId = prescriptionId;
        this.ini = ini;
        this.end = end;
        this.doseUnit = doseUnit;
        this.frequency = frequency;
        this.comments = comments;
    }

    public UUID getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(UUID prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public PrescriptionPeriod() {
        super();
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
