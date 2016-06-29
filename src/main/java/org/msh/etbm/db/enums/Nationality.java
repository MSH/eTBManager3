package org.msh.etbm.db.enums;

public enum Nationality {

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

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
