package org.msh.etbm.services.admin.regimens;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

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

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }
}
