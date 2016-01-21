package org.msh.etbm.services.admin.sources;

import org.msh.etbm.commons.entities.query.EntityQuery;

/**
 * Created by rmemoria on 11/11/15.
 */
public class SourceQueryParams extends EntityQuery {

    /**
     * If true, will include non active entities
     */
    private boolean includeDisabled;

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }
}
