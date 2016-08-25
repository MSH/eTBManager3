package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum Gender implements MessageKey {
    MALE,
    FEMALE;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
