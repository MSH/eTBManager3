package org.msh.etbm.services.cases.comorbidity;

import java.util.Optional;

/**
 * Created by msantos on 26/3/16.
 */
public class ComorbidityFormData {

    // Risk Factors and Concomitant Diagnoses
    private Optional<Boolean> alcoholExcessiveUse;
    private Optional<Boolean> tobaccoUseWithin;
    private Optional<Boolean> aids;
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

    public Optional<Boolean> getAids() {
        return aids;
    }

    public void setAids(Optional<Boolean> aids) {
        this.aids = aids;
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
