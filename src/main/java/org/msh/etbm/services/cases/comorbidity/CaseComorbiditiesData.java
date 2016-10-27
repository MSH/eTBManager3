package org.msh.etbm.services.cases.comorbidity;

/**
 * Created by Mauricio on 27/10/2016.
 */
public class CaseComorbiditiesData {

    private boolean alcoholExcessiveUse;
    private boolean tobaccoUseWithin;
    private boolean aids;
    private boolean diabetes;
    private boolean anaemia;
    private boolean malnutrition;

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
