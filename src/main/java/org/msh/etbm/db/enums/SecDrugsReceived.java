package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum SecDrugsReceived implements MessageKey {
    YES("global.yes"),
    NO("global.no"),
    UNKNOWN("manag.ind.interim.unknown");

    private final String messageKey;

    SecDrugsReceived(String msg) {
        messageKey = msg;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
}
