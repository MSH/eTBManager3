package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.Item;

import java.util.UUID;

/**
 * Information about product/medicine with minimal data returned from the product service
 * Created by rmemoria on 11/11/15.
 */
public class ProductItem extends Item<UUID> {

    private ProductType type;

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }
}
