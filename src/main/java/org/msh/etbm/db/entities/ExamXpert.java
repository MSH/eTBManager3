package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.enums.XpertResult;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "examxpert")
public class ExamXpert extends LaboratoryExam {

    @PropertyLog(operations = {Operation.ALL})
    private XpertResult result;

    @Override
    public ExamResult getExamResult() {
        if (result == null) {
            return ExamResult.UNDEFINED;
        }

        if (result == XpertResult.TB_DETECTED_RIF_DETECTED ||
                result == XpertResult.TB_DETECTED_RIF_INDETERMINATE ||
                result == XpertResult.TB_DETECTED_RIF_NOT_DETECTED) {
            return ExamResult.POSITIVE;
        }

        return ExamResult.NEGATIVE;
    }

    public XpertResult getResult() {
        return result;
    }

    public void setResult(XpertResult result) {
        this.result = result;
    }
}
