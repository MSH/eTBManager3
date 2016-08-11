package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * Possible status of an exam, starting in the request from the unit and finishes when exam is performed and results are released
 *
 * @author Ricardo Memoria
 */
public enum ExamStatus implements MessageKey {
    REQUESTED,
    ONGOING,
    PERFORMED;

    /**
     * Return the key string in the list of the messages to display the correct status in the selected language
     *
     * @return
     */
    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
