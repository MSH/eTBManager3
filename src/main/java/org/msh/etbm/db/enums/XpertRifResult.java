package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum XpertRifResult implements MessageKey {
    RIF_DETECTED,
    RIF_NOT_DETECTED,
    RIF_INDETERMINATE;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
