package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum XRayBaseline implements MessageKey {
    NORMAL,
    CAVITARY,
    INFILTRATE,
    OTHER,
    ONGOING;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
