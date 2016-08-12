package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum LocalityType implements MessageKey {

    URBAN,
    RURAL;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
