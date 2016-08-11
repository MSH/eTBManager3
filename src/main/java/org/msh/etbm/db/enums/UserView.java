package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum UserView implements MessageKey {
    COUNTRY,
    ADMINUNIT,
    UNIT,
    SELECTEDUNITS;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
