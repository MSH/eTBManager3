package org.msh.etbm.services.cases.followup.exammic;

import org.msh.etbm.db.enums.*;
import org.msh.etbm.services.cases.followup.data.LaboratoryExamData;

/**
 * Created by msantos on 13/7/16.
 */
public class ExamMicData extends LaboratoryExamData {

    private MicroscopyResult result;
    private Integer numberOfAFB;
    private SampleType sampleType;
    private String otherSampleType;
    private VisualAppearance visualAppearance;
    private String method;

    public MicroscopyResult getResult() {
        return result;
    }

    public void setResult(MicroscopyResult result) {
        this.result = result;
    }

    public Integer getNumberOfAFB() {
        return numberOfAFB;
    }

    public void setNumberOfAFB(Integer numberOfAFB) {
        this.numberOfAFB = numberOfAFB;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public String getOtherSampleType() {
        return otherSampleType;
    }

    public void setOtherSampleType(String otherSampleType) {
        this.otherSampleType = otherSampleType;
    }

    public VisualAppearance getVisualAppearance() {
        return visualAppearance;
    }

    public void setVisualAppearance(VisualAppearance visualAppearance) {
        this.visualAppearance = visualAppearance;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
