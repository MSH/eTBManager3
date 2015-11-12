package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.entities.query.EntityQuery;

/**
 * Query class to establish the criterias to return a list of substances
 *
 * Created by rmemoria on 12/11/15.
 */
public class SubstanceQuery extends EntityQuery {
    private boolean prevTreatmentForm;
    private boolean dstResultForm;

    public boolean isPrevTreatmentForm() {
        return prevTreatmentForm;
    }

    public void setPrevTreatmentForm(boolean prevTreatmentForm) {
        this.prevTreatmentForm = prevTreatmentForm;
    }

    public boolean isDstResultForm() {
        return dstResultForm;
    }

    public void setDstResultForm(boolean dstResultForm) {
        this.dstResultForm = dstResultForm;
    }
}
