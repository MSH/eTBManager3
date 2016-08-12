package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum DotBy implements MessageKey {
    HCW,
    H,
    CHW,
    ND,
    //Cambodia
    HOSPITAL,
    AMBULATORY,
    HOMECARE,
    COMMUNITY,
    NONDOT;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
