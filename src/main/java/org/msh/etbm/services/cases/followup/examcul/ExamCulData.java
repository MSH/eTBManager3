package org.msh.etbm.services.cases.followup.examcul;

import org.msh.etbm.db.enums.CultureResult;
import org.msh.etbm.db.enums.SampleType;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamData;

/**
 * Created by msantos on 14/7/16.
 */
public class ExamCulData extends LaboratoryExamData {

    private CultureResult result;
    private Integer numberOfColonies;
    private SampleType sampleType;
    private String method;

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
