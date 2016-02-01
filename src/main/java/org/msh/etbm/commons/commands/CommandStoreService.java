package org.msh.etbm.commons.commands;

/**
 * Store a command in the history of commands for future usage
 * Created by rmemoria on 17/10/15.
 */
@FunctionalInterface
public interface CommandStoreService {
    void store(CommandHistoryInput cmd);
}
