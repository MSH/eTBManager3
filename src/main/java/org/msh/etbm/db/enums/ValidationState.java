package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum ValidationState implements MessageKey {

    WAITING_VALIDATION,
    VALIDATED;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
