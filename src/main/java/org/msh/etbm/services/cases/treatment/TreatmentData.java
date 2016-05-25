package org.msh.etbm.services.cases.treatment;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.Period;

import java.util.List;

/**
 * Created by rmemoria on 23/5/16.
 */
public class TreatmentData {
    private Period period;

    private SynchronizableItem regimen;

    private SynchronizableItem iniRegimen;

    private Item<String> category;

    private int progress;

    private List<PrescribedMedicineData> prescribedMedicines;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public SynchronizableItem getRegimen() {
        return regimen;
    }

    public void setRegimen(SynchronizableItem regimen) {
        this.regimen = regimen;
    }

    public SynchronizableItem getIniRegimen() {
        return iniRegimen;
    }

    public void setIniRegimen(SynchronizableItem iniRegimen) {
        this.iniRegimen = iniRegimen;
    }

    public Item<String> getCategory() {
        return category;
    }

    public void setCategory(Item<String> category) {
        this.category = category;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public List<PrescribedMedicineData> getPrescribedMedicines() {
        return prescribedMedicines;
    }

    public void setPrescribedMedicines(List<PrescribedMedicineData> prescribedMedicines) {
        this.prescribedMedicines = prescribedMedicines;
    }
}
