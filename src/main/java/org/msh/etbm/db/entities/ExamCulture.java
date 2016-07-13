package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.enums.CultureResult;
import org.msh.etbm.db.enums.SampleType;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "examculture")
public class ExamCulture extends LaboratoryExam {

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private CultureResult result;

    @PropertyLog(operations = {Operation.NEW})
    private Integer numberOfColonies;

    private SampleType sampleType;

    @Column(length = 50)
    private String method;

    @Override
    public ExamResult getExamResult() {
        if (result == null) {
            return ExamResult.UNDEFINED;
        }

        if (result.isNegative()) {
            return ExamResult.NEGATIVE;
        }

        if (result.isPositive()) {
            return ExamResult.POSITIVE;
        }

        return ExamResult.UNDEFINED;
    }

    public CultureResult getResult() {
        return result;
    }

    public void setResult(CultureResult result) {
        this.result = result;
    }

    public Integer getNumberOfColonies() {
        return numberOfColonies;
    }

    public void setNumberOfColonies(Integer numberOfColonies) {
        this.numberOfColonies = numberOfColonies;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
