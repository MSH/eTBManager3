package org.msh.etbm.services.cases.view.unitview;

import org.msh.etbm.commons.Item;
import org.msh.etbm.db.enums.MicroscopyResult;
import org.msh.etbm.db.enums.XpertResult;

/**
 * Created by rmemoria on 2/6/16.
 */
public class PresumptiveCaseData extends CommonCaseData {

    private String caseNumber;
    private Item<XpertResult> xpertResult;
    private Item<MicroscopyResult> microscopyResult;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Item<XpertResult> getXpertResult() {
        return xpertResult;
    }

    public void setXpertResult(Item<XpertResult> xpertResult) {
        this.xpertResult = xpertResult;
    }

    public Item<MicroscopyResult> getMicroscopyResult() {
        return microscopyResult;
    }

    public void setMicroscopyResult(Item<MicroscopyResult> microscopyResult) {
        this.microscopyResult = microscopyResult;
    }
}
