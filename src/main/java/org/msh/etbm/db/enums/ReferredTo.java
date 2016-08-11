package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum ReferredTo implements MessageKey {
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

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
