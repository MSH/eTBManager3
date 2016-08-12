package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum Nationality implements MessageKey {

    NATIVE,
    FOREIGN,

    //Kenya Workspace
    KENYA,
    BURUNDI,
    ETHIOPIA,
    RWANDA,
    SOMALIA,
    SUDAN,
    TANZANIA,
    UGANDA,
    OTHER,

    //Nigeria Workspace
    NIGERIA,

    //Namibia
    NAMIBIA,
    NON_NAMIBIA,

    //Cambodia
    CAMBODIA,
    VIETNAM,
    THAILAND,
    LAO;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
