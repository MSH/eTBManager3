package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum CaseDefinition implements MessageKey {
    BACTERIOLOGICALLY_CONFIRMED,
    CLINICALLY_DIAGNOSED;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
