package org.msh.etbm.db.enums;

public enum MedicineCategory {
    INJECTABLE,
    ORAL;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
