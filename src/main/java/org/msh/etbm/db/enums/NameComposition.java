package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum NameComposition implements MessageKey {

    FULLNAME,
    FIRSTSURNAME,
    SURNAME_FIRSTNAME,
    FIRST_MIDDLE_LASTNAME,
    LAST_FIRST_MIDDLENAME,
    LAST_FIRST_MIDDLENAME_WITHOUT_COMMAS;


    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
