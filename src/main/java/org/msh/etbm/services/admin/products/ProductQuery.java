package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.entities.query.EntityQuery;

/**
 * Filters or conditions to generate a query of products
 *
 * Created by rmemoria on 11/11/15.
 */
public class ProductQuery extends EntityQuery {
    private boolean medicinesOnly;

    private String key;

    private boolean includeDisabled;

    public boolean isMedicinesOnly() {
        return medicinesOnly;
    }

    public void setMedicinesOnly(boolean medicinesOnly) {
        this.medicinesOnly = medicinesOnly;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }
}
