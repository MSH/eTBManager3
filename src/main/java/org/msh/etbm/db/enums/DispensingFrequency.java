package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum DispensingFrequency implements MessageKey {
    MONTHLY,
    WEEKLY,
    DAILY;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
