package org.msh.etbm.db.enums;

/**
 * @author Ricardo
 *         State of the case when it's validated.
 */
public enum NotificationState {
    WAITING_VALIDATION,
    VALIDATED,
    PENDING;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
