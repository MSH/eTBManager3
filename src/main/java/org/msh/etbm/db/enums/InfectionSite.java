package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum InfectionSite implements MessageKey {
    PULMONARY,
    EXTRAPULMONARY,
    BOTH;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
