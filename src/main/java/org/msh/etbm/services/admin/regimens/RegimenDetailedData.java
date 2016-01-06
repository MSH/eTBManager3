package org.msh.etbm.services.admin.regimens;

import org.msh.etbm.db.entities.MedicineRegimen;

import java.util.List;

/**
 * Created by rmemoria on 6/1/16.
 */
public class RegimenDetailedData extends RegimenData {
    private List<MedicineRegimen> medicines;

    public List<MedicineRegimen> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<MedicineRegimen> medicines) {
        this.medicines = medicines;
    }
}
