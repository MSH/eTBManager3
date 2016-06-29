package org.msh.etbm.db.enums;

public enum Gender {
    MALE,
    FEMALE;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
