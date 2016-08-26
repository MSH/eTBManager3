package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum PrevTBTreatmentOutcome implements MessageKey {

    CURED,
    COMPLETED,
    FAILURE,
    DEFAULTED,
    SCHEME_CHANGED,
    TRANSFERRED_OUT,
    SHIFT_CATIV,
    UNKNOWN,
    ONGOING,
    DIAGNOSTIC_CHANGED,
    NO_OUTCOME_YET,
    OTHER,
    /*Bangladesh*/DELAYED_CONVERTER;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
