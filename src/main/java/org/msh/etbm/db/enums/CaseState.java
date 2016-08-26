package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum CaseState implements MessageKey {
    // Patient is not on treatment
    NOT_ONTREATMENT,
    // Patient is on treatment
    ONTREATMENT,
    // Case was closed
    CLOSED;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
