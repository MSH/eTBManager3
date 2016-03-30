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

    private List<MedicineRegimenFormData> medicines;

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

    public List<MedicineRegimenFormData> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<MedicineRegimenFormData> medicines) {
        this.medicines = medicines;
    }
}
