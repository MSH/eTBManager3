package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum MedicineLine implements MessageKey {
    FIRST_LINE,
    SECOND_LINE,
    OTHER;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
