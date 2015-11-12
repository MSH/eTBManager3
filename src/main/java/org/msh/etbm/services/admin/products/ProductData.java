package org.msh.etbm.services.admin.products;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.Item;
import org.msh.etbm.db.enums.MedicineCategory;
import org.msh.etbm.db.enums.MedicineLine;

import java.util.UUID;

/**
 * Product data returned by the product service with information about a product/medicine
 *
 * Created by rmemoria on 11/11/15.
 */
public class ProductData extends Item<UUID> {

    private String shortName;

    // just available in medicines
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MedicineLine line;

    // just available in medicines
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MedicineCategory category;

    private String customId;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public MedicineLine getLine() {
        return line;
    }

    public void setLine(MedicineLine line) {
        this.line = line;
    }

    public MedicineCategory getCategory() {
        return category;
    }

    public void setCategory(MedicineCategory category) {
        this.category = category;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }
}
