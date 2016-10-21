package org.msh.etbm.services.init.demodata.data;

import org.msh.etbm.db.enums.CaseClassification;

import java.util.List;

/**
 * Stores data about Regimens created as demonstration data
 * Created by Mauricio on 19/10/2016.
 */
public class RegimenDemoData {

    private String name;

    private CaseClassification classification;

    private List<MedicineRegimenDemoData> medicineRegimenDemoDatas;

    private String customId;

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CaseClassification getClassification() {
        return classification;
    }

    public void setClassification(CaseClassification classification) {
        this.classification = classification;
    }

    public List<MedicineRegimenDemoData> getMedicineRegimenDemoDatas() {
        return medicineRegimenDemoDatas;
    }

    public void setMedicineRegimenDemoDatas(List<MedicineRegimenDemoData> medicineRegimenDemoDatas) {
        this.medicineRegimenDemoDatas = medicineRegimenDemoDatas;
    }
}
