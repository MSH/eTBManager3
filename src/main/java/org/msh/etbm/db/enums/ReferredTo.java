package org.msh.etbm.db.enums;

public enum ReferredTo {
    NUTRITION_SUPPORT,
    VCT_CENTER,
    HIV_COMP_CARE_UNIT,
    STI_CLINIC,
    HOME_BASED_CARE,
    ANTENATAL_CLINIC,
    PRIVATE_SECTOR,
    NOT_REFERRED,

    //Nigeria
    PUBLIC,
    FBO,

    //Bangladesh
    PP,
    GFS,
    NON_PP,
    SS,
    VD,
    CV,
    GOV,
    PRIVATE_HOSP,
    TB_PATIENT,
    OTHER;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
