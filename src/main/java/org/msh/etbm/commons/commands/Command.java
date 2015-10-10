package org.msh.etbm.commons.commands;

/**
 * Interface that all commands must implement in order to be chained and registered as
 * a command in the system
 * Created by rmemoria on 7/10/15.
 */
public interface Command {

    /**
     * Run a given command
     * @param request Contain data about the command to be executed
     * @return return the response of the command
     */
    Object run(Object request);
}
