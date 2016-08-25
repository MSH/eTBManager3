package org.msh.etbm.services.cases.treatment.data;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.services.cases.treatment.followup.MonthlyFollowup;

import java.util.List;

/**
 * Treatment data information to be sent to the client
 * Created by rmemoria on 23/5/16.
 */
public class TreatmentData {
    /**
     * The treatment period
     */
    private Period period;

    /**
     * The current regimen in use
     */
    private SynchronizableItem regimen;

    /**
     * The initial regimen, in case it was moved to an individualized
     */
    private SynchronizableItem regimenIni;

    /**
     * The treatment category
     */
    private Item<String> category;

    /**
     * The treatment progress according to the current date, from 0 to 100
     */
    private int progress;

    /**
     * List of products prescribed to the patient
     */
    private List<PrescriptionData> prescriptions;

    /**
     * List of treatment units that the patient has passed through
     */
    private List<TreatmentUnitData> units;

    /**
     * Information about treatment followup for each day of the treatment
     */
    private List<MonthlyFollowup> followup;

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

    public SynchronizableItem getRegimenIni() {
        return regimenIni;
    }

    public void setRegimenIni(SynchronizableItem regimenIni) {
        this.regimenIni = regimenIni;
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

    public List<PrescriptionData> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionData> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<TreatmentUnitData> getUnits() {
        return units;
    }

    public void setUnits(List<TreatmentUnitData> units) {
        this.units = units;
    }

    public List<MonthlyFollowup> getFollowup() {
        return followup;
    }

    public void setFollowup(List<MonthlyFollowup> followup) {
        this.followup = followup;
    }
}
