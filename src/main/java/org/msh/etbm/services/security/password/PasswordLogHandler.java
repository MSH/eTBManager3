package org.msh.etbm.services.security.password;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.springframework.stereotype.Component;

/**
 * Register the changes in the user settings
 * Created by rmemoria on 15/5/16.
 */
@Component
public class PasswordLogHandler implements CommandLogHandler<Object, ChangePasswordResponse> {

    /**
     * Register log for three functtionalities that changes the user password.
     */
    @Override
    public void prepareLog(CommandHistoryInput in, Object request, ChangePasswordResponse response) {
        in.setEntityId(response.getUserModifiedId());
        in.setEntityName(response.getUserModifiedName());
        in.setAction(CommandAction.EXEC);
        in.setDetailText(response.getDetail());
    }
}
