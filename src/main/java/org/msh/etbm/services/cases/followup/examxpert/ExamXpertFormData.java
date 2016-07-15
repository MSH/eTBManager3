package org.msh.etbm.services.cases.followup.examxpert;

import org.msh.etbm.db.enums.XpertResult;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamFormData;

import java.util.Optional;

/**
 * Created by msantos on 14/7/16.
 */
public class ExamXpertFormData extends LaboratoryExamFormData {

    private Optional<XpertResult> result;

    public Optional<XpertResult> getResult() {
        return result;
    }

    public void setResult(Optional<XpertResult> result) {
        this.result = result;
    }
}
