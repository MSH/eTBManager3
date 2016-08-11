package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * X-Ray evolution options
 *
 * @author Ricardo Memoria
 */
public enum XRayEvolution implements MessageKey {
    IMPROVED,
    PROGRESSED,
    STABLE,
    //====== ONLY FOR AZERBAIJAN ======
    NA;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
