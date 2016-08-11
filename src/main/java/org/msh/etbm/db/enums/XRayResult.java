package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * Possible results for X-Ray exams
 *
 * @author Ricardo Lima
 */
public enum XRayResult implements MessageKey {
    POSITIVE,
    NEGATIVE,
    NO_CHANGE,
    STABILIZED;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

}
