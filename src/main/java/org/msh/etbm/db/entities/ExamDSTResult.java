package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.enums.DstResult;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "examdstresult")
public class ExamDSTResult extends Synchronizable {


    @ManyToOne
    @JoinColumn(name = "SUBSTANCE_ID")
    private Substance substance;

    @ManyToOne
    @JoinColumn(name = "EXAM_ID")
    private ExamDST exam;

    private DstResult result;


    public Substance getSubstance() {
        return substance;
    }

    public void setSubstance(Substance substante) {
        this.substance = substante;
    }

    public DstResult getResult() {
        return result;
    }

    public void setResult(DstResult result) {
        this.result = result;
    }

    /**
     * @return the exam
     */
    public ExamDST getExam() {
        return exam;
    }

    /**
     * @param exam the exam to set
     */
    public void setExam(ExamDST exam) {
        this.exam = exam;
    }
}
