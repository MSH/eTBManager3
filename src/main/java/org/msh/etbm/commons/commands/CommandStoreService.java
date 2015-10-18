package org.msh.etbm.commons.commands;

import org.springframework.stereotype.Service;

/**
 * Store a command in the history of commands for future usage
 * Created by rmemoria on 17/10/15.
 */
public interface CommandStoreService {
    void store(CommandHistoryInput cmd);
}
