package org.msh.etbm.commons.commands;

/**
 * Store a command in the history of commands for future usage
 * Created by rmemoria on 17/10/15.
 */
@FunctionalInterface
public interface CommandStoreService {
    /**
     * Create a new command history based on the given command parameters
     * @param cmd object containing information about the command
     * @return the ID of the command
     */
    Integer store(CommandHistoryInput cmd);
}
