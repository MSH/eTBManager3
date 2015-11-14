package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;

import java.util.List;

/**
 * Detailed data about a product returned by the product service
 *
 * Created by rmemoria on 13/11/15.
 */
public class ProductDetailedData extends ProductData {

    private List<SynchronizableItem> substances;

    public List<SynchronizableItem> getSubstances() {
        return substances;
    }

    public void setSubstances(List<SynchronizableItem> substances) {
        this.substances = substances;
    }
}
