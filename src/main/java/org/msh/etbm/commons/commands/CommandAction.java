package org.msh.etbm.commons.commands;

import org.msh.etbm.db.MessageKey;

/**
 * Type of command registered in the command log history
 * Created by rmemoria on 17/10/15.
 */
public enum CommandAction implements MessageKey {
    EXEC,
    CREATE,
    UPDATE,
    DELETE;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
