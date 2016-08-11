package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum MedicineCategory implements MessageKey {
    INJECTABLE,
    ORAL;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
