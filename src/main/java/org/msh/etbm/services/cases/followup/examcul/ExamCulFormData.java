package org.msh.etbm.services.cases.followup.examcul;

import org.msh.etbm.db.enums.CultureResult;
import org.msh.etbm.db.enums.SampleType;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamFormData;

import java.util.Optional;

/**
 * Created by msantos on 13/7/16.
 */
public class ExamCulFormData extends LaboratoryExamFormData {

    private Optional<CultureResult> result;
    private Optional<Integer> numberOfColonies;
    private Optional<SampleType> sampleType;
    private Optional<String> method;

    public Optional<CultureResult> getResult() {
        return result;
    }

    public void setResult(Optional<CultureResult> result) {
        this.result = result;
    }

    public Optional<Integer> getNumberOfColonies() {
        return numberOfColonies;
    }

    public void setNumberOfColonies(Optional<Integer> numberOfColonies) {
        this.numberOfColonies = numberOfColonies;
    }

    public Optional<SampleType> getSampleType() {
        return sampleType;
    }

    public void setSampleType(Optional<SampleType> sampleType) {
        this.sampleType = sampleType;
    }

    public Optional<String> getMethod() {
        return method;
    }

    public void setMethod(Optional<String> method) {
        this.method = method;
    }
}
