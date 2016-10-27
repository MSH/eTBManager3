package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Risk Factors and Concomitant Diagnoses
 * Created by Mauricio on 27/10/2016.
 */
@Entity
@Table(name = "casecomorbidities")
public class CaseComorbidities extends Synchronizable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CASE_ID")
    @NotNull
    private TbCase tbCase;

    private boolean alcoholExcessiveUse;
    private boolean tobaccoUseWithin;
    private boolean aids;
    private boolean diabetes;
    private boolean anaemia;
    private boolean malnutrition;

    public TbCase getTbCase() {
        return tbCase;
    }

    public void setTbCase(TbCase tbCase) {
        this.tbCase = tbCase;
    }

    public boolean isAlcoholExcessiveUse() {
        return alcoholExcessiveUse;
    }

    public void setAlcoholExcessiveUse(boolean alcoholExcessiveUse) {
        this.alcoholExcessiveUse = alcoholExcessiveUse;
    }

    public boolean isTobaccoUseWithin() {
        return tobaccoUseWithin;
    }

    public void setTobaccoUseWithin(boolean tobaccoUseWithin) {
        this.tobaccoUseWithin = tobaccoUseWithin;
    }

    public boolean isAids() {
        return aids;
    }

    public void setAids(boolean aids) {
        this.aids = aids;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isAnaemia() {
        return anaemia;
    }

    public void setAnaemia(boolean anaemia) {
        this.anaemia = anaemia;
    }

    public boolean isMalnutrition() {
        return malnutrition;
    }

    public void setMalnutrition(boolean malnutrition) {
        this.malnutrition = malnutrition;
    }
}
