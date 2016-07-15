package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 10/2/16.
 */
@Component
public class UserOptions implements FormRequestHandler<List<Item>> {

    public static final String CMD_NAME = "users";

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }

    @Override
    @Transactional
    public List<Item> execFormRequest(FormRequest req) {
        List<Item> options = new ArrayList<>();
        List<User> userList = entityManager.createQuery("select uw.user from UserWorkspace uw where uw.workspace.id = :wId order by uw.user.name")
                .setParameter("wId", userRequestService.getUserSession().getWorkspaceId())
                .getResultList();

        for (User u : userList) {
            options.add(new Item(u.getId(), u.getName()));
        }

        return options;
    }
}
