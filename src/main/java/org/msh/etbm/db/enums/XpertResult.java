package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum XpertResult implements MessageKey {

    INVALID,
    ERROR,
    NO_RESULT,
    ONGOING,
    TB_NOT_DETECTED,
    TB_DETECTED,
    INVALID_NORESULT_ERROR;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
