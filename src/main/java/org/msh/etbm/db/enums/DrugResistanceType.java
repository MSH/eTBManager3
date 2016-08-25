package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum DrugResistanceType implements MessageKey {

    MONO_RESISTANCE,
    POLY_RESISTANCE,
    MULTIDRUG_RESISTANCE,
    EXTENSIVEDRUG_RESISTANCE,
    RIFAMPICIN_MONO_RESISTANCE,
    ISONIAZID_MONO_RESISTANCE,
    //Bangladesh
    UNKNOWN,
    //Cambodia
    RIF_RESISTANCE;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
