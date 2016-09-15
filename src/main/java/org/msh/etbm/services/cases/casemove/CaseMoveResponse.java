package org.msh.etbm.services.cases.casemove;

import org.msh.etbm.services.cases.CaseActionResponse;

/**
 * Created by Mauricio on 14/09/2016.
 */
public class CaseMoveResponse extends CaseActionResponse {

    String currentOwnerUnitName;
    String currentOwnerUnitAU;

    String previousOwnerUnitName;
    String previousOwnerUnitAU;

    public String getCurrentOwnerUnitName() {
        return currentOwnerUnitName;
    }

    public void setCurrentOwnerUnitName(String currentOwnerUnitName) {
        this.currentOwnerUnitName = currentOwnerUnitName;
    }

    public String getCurrentOwnerUnitAU() {
        return currentOwnerUnitAU;
    }

    public void setCurrentOwnerUnitAU(String currentOwnerUnitAU) {
        this.currentOwnerUnitAU = currentOwnerUnitAU;
    }

    public String getPreviousOwnerUnitName() {
        return previousOwnerUnitName;
    }

    public void setPreviousOwnerUnitName(String previousOwnerUnitName) {
        this.previousOwnerUnitName = previousOwnerUnitName;
    }

    public String getPreviousOwnerUnitAU() {
        return previousOwnerUnitAU;
    }

    public void setPreviousOwnerUnitAU(String previousOwnerUnitAU) {
        this.previousOwnerUnitAU = previousOwnerUnitAU;
    }
}
