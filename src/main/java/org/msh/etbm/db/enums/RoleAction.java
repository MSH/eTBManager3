package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * Actions available for each role
 *
 * @author Ricardo Memoria
 */
public enum RoleAction implements MessageKey {

    VIEW,
    NEW,
    EDIT,
    DELETE,
    EXEC;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
