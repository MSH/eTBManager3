package org.msh.etbm.services.admin.substances;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

/**
 * Query class to establish the criterias to return a list of substances
 * <p>
 * Created by rmemoria on 12/11/15.
 */
public class SubstanceQueryParams extends EntityQueryParams {
    /**
     * Available profiles
     */
    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    /**
     * Available sorting options
     */
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_DISPLAYORDER = "displayOrder";


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
