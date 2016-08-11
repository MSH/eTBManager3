package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum NameComposition implements MessageKey {

    FULLNAME(1),
    FIRSTSURNAME(2),
    SURNAME_FIRSTNAME(2),
    FIRST_MIDDLE_LASTNAME(3),
    LAST_FIRST_MIDDLENAME(3),
    LAST_FIRST_MIDDLENAME_WITHOUT_COMMAS(3);

    private int numFields;

    NameComposition(int numFields) {
        this.numFields = numFields;
    }

    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

    public int getNumFields() {
        return numFields;
    }
}
