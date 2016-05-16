package org.msh.etbm.services.usersettings;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.objutils.DiffValue;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.db.entities.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

/**
 * Register the changes in the user settings
 * Created by rmemoria on 15/5/16.
 */
@Component
public class UserSettingsLogHandler implements CommandLogHandler<UserSettingsFormData, Diffs> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void prepareLog(CommandHistoryInput in, UserSettingsFormData request, Diffs response) {
        // mount the list of differences
        for (Map.Entry<String, DiffValue> entry: response.getValues().entrySet()) {
            in.addDiff(entry.getKey(), entry.getValue().getPrevValue(), entry.getValue().getNewValue());
        }

        in.setEntityId(in.getUserId());

        User user = entityManager.find(User.class, in.getUserId());
        in.setEntityName(user.getName());
        in.setAction(CommandAction.EXEC);
    }
}
