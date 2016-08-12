package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum SampleType implements MessageKey {

    SPUTUM,
    OTHER,

    //Cambodia
    PUS,
    CSF,
    URINE,
    STOOL,
    TISSUE;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
