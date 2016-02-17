package org.msh.etbm.services.init.impl;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.services.init.RegisterWorkspaceRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
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

        in.beginListLog()
                .add("Workspace.adminEmail", request.getAdminEmail());
    }
}
