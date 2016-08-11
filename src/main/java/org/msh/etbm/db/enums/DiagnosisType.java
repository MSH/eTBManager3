package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum DiagnosisType implements MessageKey {

    SUSPECT,
    CONFIRMED;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
