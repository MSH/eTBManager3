package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum HIVResult implements MessageKey {
    POSITIVE,
    NEGATIVE,
    ONGOING,
    NOTDONE;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
