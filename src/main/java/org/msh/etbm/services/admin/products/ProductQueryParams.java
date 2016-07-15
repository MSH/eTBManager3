package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

/**
 * Filters or conditions to generate a query of products
 * <p>
 * Created by rmemoria on 11/11/15.
 */
public class ProductQueryParams extends EntityQueryParams {
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_SHORTNAME = "shortName";
    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";


    private ProductType type;

    private String key;

    private boolean includeDisabled;

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
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
