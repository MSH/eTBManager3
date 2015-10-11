package org.msh.etbm.commons.commands.impl;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.commands.Command;
import org.msh.etbm.commons.commands.CommandManager;
import org.msh.etbm.commons.commands.CommandPreparation;
import org.msh.etbm.commons.commands.CommandsFactory;
import org.msh.etbm.db.entities.UserWorkspace;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the command service interface. Singleton object to expose
 * a command service object
 *
 * Created by rmemoria on 7/10/15.
 */
public class CommandManagerImpl implements CommandManager {


    /**
     * The entity manager in use
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Information about the user session
     */
    @Autowired
    private UserWorkspace userWorkspace;

    /**
     * Dozer object mapping
     */
    @Autowired
    private DozerBeanMapper mapper;


    private Map<String, Command> commands = new HashMap<>();


    @Override
    public Object run(String type, Object data) {
        Command cmd = commandByType(type);
        if (cmd == null) {
            throw new RuntimeException("No command found for type " + type);
        }

        // data must be prepared before execution ?
        if (cmd instanceof CommandPreparation) {
            CommandPreparation cmdPrep = (CommandPreparation)cmd;
            data = cmdPrep.prepareCommandData(data);
        }

        Object res = cmd.run(data);

        return res;
    }


    /**
     * Search for a command handler by its type
     * @param type
     * @return
     */
    protected Command commandByType(String type) {
        return commands.get(type);
    }

    @Override
    public void register(String type, Command command) {
        if (commands.get(type) != null) {
            throw new RuntimeException("Command of type " + type + " is already registered");
        }

        commands.put(type, command);
    }

    @Override
    public void register(CommandsFactory commandsFactory) {
        Map<String, Command> cmds = commandsFactory.commandsMapping();

        if (cmds != null) {
            for (String type: cmds.keySet()) {
                Command cmd = cmds.get(type);
                register(type, cmd);
            }
        }
    }
}
