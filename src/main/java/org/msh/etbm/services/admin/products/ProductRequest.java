package org.msh.etbm.services.admin.products;

import org.msh.etbm.db.enums.MedicineCategory;
import org.msh.etbm.db.enums.MedicineLine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Request class used as input data to save or update a product/medicine
 * Created by rmemoria on 11/11/15.
 */
public class ProductRequest {
    /** if true, this product represents a medicine */
    private Boolean medicine;

    private Optional<String> name;
    private Optional<String> shortName;
    private Optional<String> customId;

    // used just in medicines
    private Optional<MedicineCategory> category;
    private Optional<MedicineLine> line;

    private List<UUID> substances;


    public Boolean getMedicine() {
        return medicine;
    }

    public void setMedicine(Boolean medicine) {
        this.medicine = medicine;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getShortName() {
        return shortName;
    }

    public void setShortName(Optional<String> shortName) {
        this.shortName = shortName;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }

    public Optional<MedicineCategory> getCategory() {
        return category;
    }

    public void setCategory(Optional<MedicineCategory> category) {
        this.category = category;
    }

    public Optional<MedicineLine> getLine() {
        return line;
    }

    public void setLine(Optional<MedicineLine> line) {
        this.line = line;
    }

    public List<UUID> getSubstances() {
        return substances;
    }

    public void setSubstances(List<UUID> substances) {
        this.substances = substances;
    }
}
