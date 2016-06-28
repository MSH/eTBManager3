package org.msh.etbm.services.security.password;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.db.entities.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Register the changes in the user settings
 * Created by rmemoria on 15/5/16.
 */
@Component
public class PasswordLogHandler implements CommandLogHandler<Object, Diffs> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void prepareLog(CommandHistoryInput in, Object request, Diffs response) {
        switch(in.getType()){
            case "userSessionChangePassword":
                User user = entityManager.find(User.class, in.getUserId());

                in.setEntityId(in.getUserId());
                in.setEntityName(user.getName());
                in.setAction(CommandAction.EXEC);
                in.setDetailText("User session Change Password");
                break;

            case "userWsChangePassword":
                User userPwdChanged = entityManager.find(User.class, response.get("userPwdChanged").getNewValue());

                in.setEntityId(userPwdChanged.getId());
                in.setEntityName(userPwdChanged.getName());
                in.setAction(CommandAction.EXEC);
                in.setDetailText("User password changed");
                break;
        }
    }
}
