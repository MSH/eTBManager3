package org.msh.etbm.services.admin.regimens;

import org.msh.etbm.db.entities.MedicineRegimen;
import org.msh.etbm.db.enums.CaseClassification;

import java.util.List;
import java.util.Optional;

/**
 * Created by rmemoria on 6/1/16.
 */
public class RegimenFormData {
    private Optional<String> name;

    private Optional<CaseClassification> classification;

    private Optional<String> customId;

    private Optional<Boolean> active;

    private Optional<List<MedicineRegimen>> medicines; //TODOMSR: deveria fazer um formdata para MedicineRegimen?

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<CaseClassification> getClassification() {
        return classification;
    }

    public void setClassification(Optional<CaseClassification> classification) {
        this.classification = classification;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }

    public Optional<Boolean> getActive() {
        return active;
    }

    public void setActive(Optional<Boolean> active) {
        this.active = active;
    }

    public Optional<List<MedicineRegimen>> getMedicines() {
        return medicines;
    }

    public void setMedicines(Optional<List<MedicineRegimen>> medicines) {
        this.medicines = medicines;
    }
}
