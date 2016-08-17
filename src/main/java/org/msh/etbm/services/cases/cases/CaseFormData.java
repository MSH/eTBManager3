package org.msh.etbm.services.cases.cases;

import java.util.Optional;

/**
 * Created by msantos on 26/3/16.
 */
public class CaseFormData {
    //TODO: include other relevant case fields

    private Optional<Boolean> alcoholExcessiveUse;
    private Optional<Boolean> tobaccoUseWithin;
    private Optional<Boolean> hivPositive;
    private Optional<Boolean> diabetes;
    private Optional<Boolean> anaemia;
    private Optional<Boolean> malnutrition;

    public Optional<Boolean> getAlcoholExcessiveUse() {
        return alcoholExcessiveUse;
    }

    public void setAlcoholExcessiveUse(Optional<Boolean> alcoholExcessiveUse) {
        this.alcoholExcessiveUse = alcoholExcessiveUse;
    }

    public Optional<Boolean> getTobaccoUseWithin() {
        return tobaccoUseWithin;
    }

    public void setTobaccoUseWithin(Optional<Boolean> tobaccoUseWithin) {
        this.tobaccoUseWithin = tobaccoUseWithin;
    }

    public Optional<Boolean> getHivPositive() {
        return hivPositive;
    }

    public void setHivPositive(Optional<Boolean> hivPositive) {
        this.hivPositive = hivPositive;
    }

    public Optional<Boolean> getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(Optional<Boolean> diabetes) {
        this.diabetes = diabetes;
    }

    public Optional<Boolean> getAnaemia() {
        return anaemia;
    }

    public void setAnaemia(Optional<Boolean> anaemia) {
        this.anaemia = anaemia;
    }

    public Optional<Boolean> getMalnutrition() {
        return malnutrition;
    }

    public void setMalnutrition(Optional<Boolean> malnutrition) {
        this.malnutrition = malnutrition;
    }
}
