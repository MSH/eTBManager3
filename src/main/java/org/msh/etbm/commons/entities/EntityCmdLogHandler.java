package org.msh.etbm.commons.entities;

import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.springframework.stereotype.Component;

/**
 * Generic command log handler to support CUD operations
 *
 * Created by rmemoria on 25/10/15.
 */
@Component
public class EntityCmdLogHandler implements CommandLogHandler {
    @Override
    public CommandHistoryInput prepareLog(CommandHistoryInput in, Object request, Object response) {
        return null;
    }
}
