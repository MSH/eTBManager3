package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.entities.query.EntityQuery;

/**
 * Query class to establish the criterias to return a list of substances
 *
 * Created by rmemoria on 12/11/15.
 */
public class SubstanceQueryParams extends EntityQuery {
    private boolean prevTreatmentForm;
    private boolean dstResultForm;

    private boolean includeDisabled;

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

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }
}
