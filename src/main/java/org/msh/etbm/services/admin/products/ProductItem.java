package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.Item;

import java.util.UUID;

/**
 * Information about product/medicine with minimal data returned from the product service
 * Created by rmemoria on 11/11/15.
 */
public class ProductItem extends Item<UUID> {

    private boolean medicine;

    public boolean isMedicine() {
        return medicine;
    }

    public void setMedicine(boolean medicine) {
        this.medicine = medicine;
    }
}
