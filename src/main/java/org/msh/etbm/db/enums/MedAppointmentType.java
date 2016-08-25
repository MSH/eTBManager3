package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum MedAppointmentType implements MessageKey {

    SCHEDULLED,
    EXTRA;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
