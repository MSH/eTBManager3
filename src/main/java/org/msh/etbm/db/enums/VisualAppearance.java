package org.msh.etbm.db.enums;

/**
 * Possible status of an exam, starting in the request from the unit and finishes when exam is performed and results are released
 *
 * @author Ricardo Memoria
 */
public enum VisualAppearance {
    BLOOD_STAINED,
    MUCOPURULENT,
    SALIVA;

    /**
     * Return the key string in the list of the messages to display the correct status in the selected language
     *
     * @return
     */
    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
