package org.msh.etbm.db.enums;

public enum ValidationState {

    WAITING_VALIDATION,
    VALIDATED;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
