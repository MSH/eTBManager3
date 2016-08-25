package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 25/07/2016.
 */
@Component
public class ManualAssignedTagOptions implements FormRequestHandler<List<Item>> {

    public static final String CMD_NAME = "manualTags";

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
        List<Tag> medList = entityManager.createQuery("from Tag t " +
                "where t.sqlCondition is null and t.workspace.id = :wId and t.active = :true " +
                "order by t.name")
                .setParameter("wId", userRequestService.getUserSession().getWorkspaceId())
                .setParameter("true", true)
                .getResultList();

        for (Tag t : medList) {
            options.add(new Item(t.getId(), t.getName()));
        }

        return options;
    }
}
