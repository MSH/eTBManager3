package org.msh.etbm.services.cases.followup.data;

/**
 * Created by Mauricio on 07/07/2016.
 */
public enum FollowUpType {

    MEDICAL_EXAMINATION("MedicalExamination"),
    EXAM_MICROSCOPY("ExamMicroscopy"),
    EXAM_CULTURE("ExamCulture"),
    EXAM_EXPERT("ExamXpert"),
    EXAM_DST("ExamDST"),
    EXAM_XRAY("ExamXRay"),
    EXAM_HIV("ExamHIV");

    FollowUpType(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    String entityClassName;

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
