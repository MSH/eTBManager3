package org.msh.etbm.db.enums;


import org.msh.etbm.db.MessageKey;

public enum OrderStatus implements MessageKey {
    WAITAUTHORIZING,
    WAITSHIPMENT,
    SHIPPED,
    RECEIVED,
    CANCELLED,
    PREPARINGSHIPMENT;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
