package org.msh.etbm.services.cases.view.unitview;

import org.msh.etbm.db.enums.MicroscopyResult;
import org.msh.etbm.db.enums.XpertResult;

/**
 * Created by rmemoria on 2/6/16.
 */
public class PresumptiveCaseData extends CommonCaseData {

    private String caseNumber;
    private XpertResult xpertResult;
    private MicroscopyResult microscopyResult;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public XpertResult getXpertResult() {
        return xpertResult;
    }

    public void setXpertResult(XpertResult xpertResult) {
        this.xpertResult = xpertResult;
    }

    public MicroscopyResult getMicroscopyResult() {
        return microscopyResult;
    }

    public void setMicroscopyResult(MicroscopyResult microscopyResult) {
        this.microscopyResult = microscopyResult;
    }
}
