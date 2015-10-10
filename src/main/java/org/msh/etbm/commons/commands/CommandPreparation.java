package org.msh.etbm.commons.commands;

/**
 * Commands that must prepare its data before execution must implement this method
 * Created by rmemoria on 9/10/15.
 */
public interface CommandPreparation {

    /**
     * Prepare the data to be executed by the command
     * @param data
     * @return
     */
    Object prepareCommandData(Object data);
}
