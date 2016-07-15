package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * Define options of how the system will display the case number of the patient
 *
 * @author Ricardo Memoria
 */
public enum DisplayCaseNumber implements MessageKey {

    SYSTEM_GENERATED,
    USER_DEFINED;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
