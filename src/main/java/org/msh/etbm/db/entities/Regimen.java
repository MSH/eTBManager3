package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.CaseClassification;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "regimen")
public class Regimen extends WorkspaceEntity {

    @Column(length = 100)
    @NotNull
    private String name;

    private CaseClassification classification;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "regimen", orphanRemoval = true)
    private List<MedicineRegimen> medicines = new ArrayList<MedicineRegimen>();

    @Column(length = 50)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    private boolean active;


    /**
     * Return number of days of treatment
     * @return
     */
    public int getDaysTreatment() {
        int res = 0;
        for (MedicineRegimen mr: medicines) {
            int days = mr.getDays() + mr.getIniDay();
            if (days > res) {
                res = days;
            }
        }

        return res;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return (name != null ? name : super.toString());
    }

    /**
     * Check if medicine is part of the regimen
     *
     * @param med
     * @return
     */
    public boolean isMedicineInRegimen(Medicine med) {
        for (MedicineRegimen aux : getMedicines()) {
            if (aux.getMedicine().equals(med)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MedicineRegimen> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<MedicineRegimen> medicines) {
        this.medicines = medicines;
    }


    /**
     * @return the mdrTreatment
     */
    public boolean isMdrTreatment() {
        return CaseClassification.DRTB.equals(classification);
    }


    /**
     * @return the tbTreatment
     */
    public boolean isTbTreatment() {
        return CaseClassification.TB.equals(classification);
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public CaseClassification getClassification() {
        return classification;
    }

    public void setClassification(CaseClassification classification) {
        this.classification = classification;
    }

    @Override
    public String getDisplayString() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
