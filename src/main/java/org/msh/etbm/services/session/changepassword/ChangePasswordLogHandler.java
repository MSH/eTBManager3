package org.msh.etbm.services.session.changepassword;

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
public class ChangePasswordLogHandler implements CommandLogHandler<ChangePasswordFormData, Diffs> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void prepareLog(CommandHistoryInput in, ChangePasswordFormData request, Diffs response) {
        in.setEntityId(in.getUserId());

        User user = entityManager.find(User.class, in.getUserId());
        in.setEntityName(user.getName());
        in.setAction(CommandAction.EXEC);
        in.setDetailText("Change Password");
    }
}
