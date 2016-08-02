package org.msh.etbm.services.cases.followup.exammic;

import org.msh.etbm.db.enums.MicroscopyResult;
import org.msh.etbm.db.enums.SampleType;
import org.msh.etbm.db.enums.VisualAppearance;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamFormData;

import java.util.Optional;

/**
 * Created by msantos on 13/7/16.
 */
public class ExamMicFormData extends LaboratoryExamFormData {

    private Optional<MicroscopyResult> result;
    private Optional<Integer> numberOfAFB;
    private Optional<SampleType> sampleType;
    private Optional<String> otherSampleType;
    private Optional<VisualAppearance> visualAppearance;
    private Optional<String> method;

    public Optional<MicroscopyResult> getResult() {
        return result;
    }

    public void setResult(Optional<MicroscopyResult> result) {
        this.result = result;
    }

    public Optional<Integer> getNumberOfAFB() {
        return numberOfAFB;
    }

    public void setNumberOfAFB(Optional<Integer> numberOfAFB) {
        this.numberOfAFB = numberOfAFB;
    }

    public Optional<SampleType> getSampleType() {
        return sampleType;
    }

    public void setSampleType(Optional<SampleType> sampleType) {
        this.sampleType = sampleType;
    }

    public Optional<String> getOtherSampleType() {
        return otherSampleType;
    }

    public void setOtherSampleType(Optional<String> otherSampleType) {
        this.otherSampleType = otherSampleType;
    }

    public Optional<VisualAppearance> getVisualAppearance() {
        return visualAppearance;
    }

    public void setVisualAppearance(Optional<VisualAppearance> visualAppearance) {
        this.visualAppearance = visualAppearance;
    }

    public Optional<String> getMethod() {
        return method;
    }

    public void setMethod(Optional<String> method) {
        this.method = method;
    }
}
