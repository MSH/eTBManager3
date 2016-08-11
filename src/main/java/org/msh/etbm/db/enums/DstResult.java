package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum DstResult implements MessageKey {
    NOTDONE,
    RESISTANT,
    SUSCEPTIBLE,
    CONTAMINATED,
    BASELINE,
    INTERMEDIATE,
    ERROR,
    NOTRESISTANT,
    ONGOING;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
