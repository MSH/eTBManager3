package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum TransferStatus implements MessageKey {
    WAITING_RECEIVING,
    DONE,
    CANCELLED;

    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
