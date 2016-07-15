package org.msh.etbm.db.enums;

public enum LocalityType {

    URBAN,
    RURAL;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
