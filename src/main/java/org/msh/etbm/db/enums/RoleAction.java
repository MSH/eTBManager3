package org.msh.etbm.db.enums;

/**
 * Actions available for each role
 *
 * @author Ricardo Memoria
 */
public enum RoleAction {

    VIEW,
    NEW,
    EDIT,
    DELETE,
    EXEC;

    public String getKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
