package org.msh.etbm.services.cases.cases.data;

import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;

/**
 * Created by Mauricio on 08/09/2016.
 */
public class NewNotificationInitFormReq {

    private DiagnosisType diagnosisType;

    private CaseClassification caseClassification;

    public DiagnosisType getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(DiagnosisType diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public CaseClassification getCaseClassification() {
        return caseClassification;
    }

    public void setCaseClassification(CaseClassification caseClassification) {
        this.caseClassification = caseClassification;
    }
}
