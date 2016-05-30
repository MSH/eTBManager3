package org.msh.etbm.services.cases.treatment.data;

import org.msh.etbm.commons.SynchronizableItem;

import java.util.List;

/**
 * Information of prescription of a given product
 * Created by rmemoria on 23/5/16.
 */
public class PrescriptionData {

    /**
     * The product (or medicine) prescribed to the patient
     */
    private SynchronizableItem product;

    /**
     * The periods of prescription and its dosage and weekly frequency
     */
    private List<PrescriptionPeriod> periods;


    public PrescriptionData(SynchronizableItem product, List<PrescriptionPeriod> periods) {
        super();
        this.product = product;
        this.periods = periods;
    }

    public PrescriptionData() {
        super();
    }

    public SynchronizableItem getProduct() {
        return product;
    }

    public void setProduct(SynchronizableItem product) {
        this.product = product;
    }

    public List<PrescriptionPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PrescriptionPeriod> periods) {
        this.periods = periods;
    }
}
