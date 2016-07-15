package org.msh.etbm.db.enums;

public enum NameComposition {

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

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }

    public int getNumFields() {
        return numFields;
    }
}
