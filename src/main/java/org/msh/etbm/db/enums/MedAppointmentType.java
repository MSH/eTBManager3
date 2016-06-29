package org.msh.etbm.db.enums;

public enum MedAppointmentType {

    SCHEDULLED,
    EXTRA;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
