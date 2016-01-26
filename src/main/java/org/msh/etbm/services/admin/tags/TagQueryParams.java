package org.msh.etbm.services.admin.tags;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

/**
 * Created by rmemoria on 6/1/16.
 */
public class TagQueryParams extends EntityQueryParams {

    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_TYPE = "type";


    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_ITEM = "item";

    private boolean includeDisabled;

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }
}
