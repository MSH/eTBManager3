package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum PatientType implements MessageKey {
    NEW,
    RELAPSE,
    AFTER_DEFAULT,
    FAILURE_FT,
    FAILURE_RT,
    OTHER,
    // new types requested by Philippines
    FAILURE_CATI,
    FAILURE_CATII,
    FAILURE_CATIV,
    RELAPSE_CATI,
    RELAPSE_CATII,
    RELAPSE_CATIV,
    TRANSFER_IN,
    OTHER_NONDOTS,
    OTHERPOS,
    OTHERNEG,
    // new types used by Brazil
    SCHEMA_CHANGED,
    // Ukraine
    FAILURE,
    // Uzbekistan
    ANOTHER_TB,

    // Generic - Incidence Report
    ALL_RETREATMENT,
    //Brasil
    RESISTANCE_PATTERN_CHANGED,

    //Cambodia
    RAD,
    PREV_MDRTB,
    MDRTB_CONTACT,
    TB_HIV,
    // Vietnam
    FAILURE_CATI_2ND,
    FAILURE_CATI_3RD,
    FAILURE_CATII_2ND,
    FAILURE_CATII_3RD,
    CUSTOM1, CUSTOM2, CUSTOM3, CUSTOM4, CUSTOM5, CUSTOM6,
    CUSTOM7, CUSTOM8, CUSTOM9, CUSTOM10, CUSTOM11, CUSTOM12, CUSTOM13, CUSTOM14, CUSTOM15,

    //WHO recomendations - Generic
    PREVIOUSLY_TREATED,//44
    TREATMENT_AFTER_FAILURE,
    TREATMENT_AFTER_LOSS_FOLLOW_UP,
    OTHER_PREVIOUSLY_TREATED,
    UNKNOWN_PREVIOUS_TB_TREAT,//48
    ;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
