package org.msh.etbm.services.security.password;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Register the changes in the user settings
 * Created by rmemoria on 15/5/16.
 */
@Component
public class PasswordLogHandler implements CommandLogHandler<Object, Map<String, Object>> {

    /**
     * Register log for three functtionalities that changes the user password.
     */
    @Override
    public void prepareLog(CommandHistoryInput in, Object request, Map<String, Object> data) {
        in.setEntityId((UUID)data.get("userModifiedId"));
        in.setEntityName((String)data.get("userModifiedName"));
        in.setAction(CommandAction.EXEC);
        in.setDetailText((String)data.get("detail"));
    }
}
