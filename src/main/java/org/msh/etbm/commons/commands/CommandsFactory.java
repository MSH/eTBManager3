package org.msh.etbm.commons.commands;

import java.util.Map;

/**
 * Define an interface that can be used as another way to register commands in the
 * commands manager. By registering an object with this interface, multiple commands
 * can be registered at the same time
 *
 * Created by rmemoria on 9/10/15.
 */
public interface CommandsFactory {

    /**
     * Return a list of commands and its assigned types
     * @return
     */
    Map<String, Command> commandsMapping();
}
