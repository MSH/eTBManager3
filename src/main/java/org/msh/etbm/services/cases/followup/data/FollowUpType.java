package org.msh.etbm.services.cases.followup.data;

import org.msh.etbm.db.MessageKey;
import org.msh.etbm.services.security.permissions.Permissions;

/**
 * Created by Mauricio on 07/07/2016.
 */
public enum FollowUpType implements MessageKey {

    MEDICAL_EXAMINATION("MedicalExamination",
            "org.msh.etbm.services.cases.followup.medexam.MedExamData",
            Permissions.CASES_MED_EXAM),

    EXAM_MICROSCOPY("ExamMicroscopy",
            "org.msh.etbm.services.cases.followup.exammic.ExamMicData",
            Permissions.CASES_EXAM_MICROSCOPY),

    EXAM_CULTURE("ExamCulture",
            "org.msh.etbm.services.cases.followup.examculture.ExamCulData",
            Permissions.CASES_EXAM_CULTURE),

    EXAM_XPERT("ExamXpert",
            "org.msh.etbm.services.cases.followup.examxpert.ExamXpertData",
            Permissions.CASES_EXAM_XPERT),

    EXAM_DST("ExamDST",
            "org.msh.etbm.services.cases.followup.examdst.ExamDSTData",
            Permissions.CASES_EXAM_DST),

    EXAM_XRAY("ExamXRay",
            "org.msh.etbm.services.cases.followup.examxray.ExamXRayData",
            Permissions.CASES_EXAM_XRAY),

    EXAM_HIV("ExamHIV",
            "org.msh.etbm.services.cases.followup.examhiv.ExamHIVData",
            Permissions.CASES_EXAM_HIV);


    FollowUpType(String entityClassName, String dataClassCanonicalName, String permission) {
        this.entityClassName = entityClassName;
        this.dataClassCanonicalName = dataClassCanonicalName;
        this.permission = permission;
    }

    String entityClassName;
    String dataClassCanonicalName;
    String permission;

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getDataClassCanonicalName() {
        return dataClassCanonicalName;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
