package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum CaseState implements MessageKey {
    NOT_ONTREATMENT,
    ONTREATMENT,
    CLOSED;

    //    TRANSFERRING,
    //    CURED,
    //    TREATMENT_COMPLETED,
    //    FAILED,
    //    DEFAULTED,
    //    DIED,
    //    TRANSFERRED_OUT,
    //    DIAGNOSTIC_CHANGED,
    //    OTHER,
    //    MDR_CASE,
    //    TREATMENT_INTERRUPTION,
    //    NOT_CONFIRMED,
    //    DIED_NOTTB,
    //    REGIMEN_CHANGED,
    //    //New Generic Options
    //    NOT_EVALUATED,
    //    MOVED_SECONDLINE,
    //    CLOSED;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
