package org.msh.etbm.services.cases.followup.examxpert;

import org.msh.etbm.db.enums.CultureResult;
import org.msh.etbm.db.enums.SampleType;
import org.msh.etbm.db.enums.XpertResult;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamData;

/**
 * Created by msantos on 14/7/16.
 */
public class ExamXpertData extends LaboratoryExamData {

    private XpertResult result;

    public XpertResult getResult() {
        return result;
    }

    public void setResult(XpertResult result) {
        this.result = result;
    }

}
