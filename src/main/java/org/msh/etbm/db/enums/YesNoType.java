package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum YesNoType implements MessageKey {
    YES("global.yes"),
    NO("global.no");

    private final String messageKey;

    YesNoType(String msg) {
        messageKey = msg;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    public boolean isNo() {
        return this.ordinal() == YesNoType.NO.ordinal();
    }

    public boolean isYes() {
        return this.ordinal() == YesNoType.YES.ordinal();
    }
}
