package org.msh.etbm.services.init;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.db.entities.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Register in the command log the initialization of the system by registering a new workspace
 *
 * Created by rmemoria on 17/10/15.
 */
@Component
public class RegisterWorkspaceLog implements CommandLogHandler<RegisterWorkspaceRequest, UUID> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void prepareLog(CommandHistoryInput in, RegisterWorkspaceRequest request, UUID response) {
        in.setAction(CommandAction.EXEC);
        in.setEntityName(request.getWorkspaceName());
        in.setEntityId(response);

        User user = (User)entityManager.createQuery("from User where email = :email")
                .setParameter("email", request.getAdminEmail())
                .getSingleResult();

        in.setUserId(user.getId());

        in.addItem("$Workspace.adminEmail", request.getAdminEmail());
    }
}
