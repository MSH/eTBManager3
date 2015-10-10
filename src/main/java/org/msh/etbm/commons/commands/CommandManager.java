package org.msh.etbm.commons.commands;


/**
 * Interface exposed by the command service
 * Created by rmemoria on 7/10/15.
 */
public interface CommandManager {

    /**
     * Run a given command
     * @param type the command type to run
     * @param data the data to be passed to the command
     * @return the data returned by the command
     */
    Object run(String type, Object data);


    /**
     * Register a command handler
     * @param type the type of the command
     * @param command instance of the command assigned to the type
     */
    void register(String type, Command command);

    /**
     * Register multiple commands using an object implementing the {@link CommandsFactory}
     * @param commandsFactory factory that will return the list of commands to be registered
     */
    void register(CommandsFactory commandsFactory);
}
