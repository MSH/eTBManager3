package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * @author Ricardo
 *         State of the case when it's validated.
 */
public enum NotificationState implements MessageKey {
    WAITING_VALIDATION,
    VALIDATED,
    PENDING;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
