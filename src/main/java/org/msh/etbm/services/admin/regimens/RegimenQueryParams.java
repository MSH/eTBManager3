package org.msh.etbm.services.admin.regimens;

import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.enums.CaseClassification;

/**
 * Created by rmemoria on 6/1/16.
 */
public class RegimenQueryParams extends EntityQueryParams {
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_CLASSIFICATION = "classification";


    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";
    public static final String PROFILE_ITEM = "item";

    /**
     * If true, will include disabled items in the result query
     */
    private boolean includeDisabled;

    private CaseClassification caseClassification;

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }

    public CaseClassification getCaseClassification() {
        return caseClassification;
    }

    public void setCaseClassification(CaseClassification caseClassification) {
        this.caseClassification = caseClassification;
    }
}
