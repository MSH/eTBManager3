package org.msh.etbm.db.entities;

import org.msh.etbm.db.enums.MedicineCategory;
import org.msh.etbm.db.enums.MedicineLine;
import org.msh.etbm.services.admin.products.ProductType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("med")
public class Medicine extends Product {

    @NotNull
    private MedicineCategory category;

    @NotNull
    private MedicineLine line;

    @ManyToMany
    @JoinTable(name = "medicine_substances",
            joinColumns = {@JoinColumn(name = "MEDICINE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "SUBSTANCE_ID")})
    private List<Substance> substances = new ArrayList<>();

    public String getTbInfoKey() {
        return line != null ? line.getMessageKey() : null;
    }

    @Override
    public ProductType getType() {
        return ProductType.MEDICINE;
    }

    public MedicineCategory getCategory() {
        return category;
    }

    public void setCategory(MedicineCategory category) {
        this.category = category;
    }

    public MedicineLine getLine() {
        return line;
    }

    public void setLine(MedicineLine line) {
        this.line = line;
    }

    public List<Substance> getSubstances() {
        return substances;
    }

    public void setSubstances(List<Substance> substances) {
        this.substances = substances;
    }
}
