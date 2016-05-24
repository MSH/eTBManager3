package org.msh.etbm.services.cases.treatment;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.Period;

/**
 * Created by rmemoria on 23/5/16.
 */
public class PrescribedMedicineData {

    private Period period;
    private SynchronizableItem product;
    private SynchronizableItem source;
    private int doseUnit;
    private int frequency;
    private String comments;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public SynchronizableItem getProduct() {
        return product;
    }

    public void setProduct(SynchronizableItem product) {
        this.product = product;
    }

    public SynchronizableItem getSource() {
        return source;
    }

    public void setSource(SynchronizableItem source) {
        this.source = source;
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
