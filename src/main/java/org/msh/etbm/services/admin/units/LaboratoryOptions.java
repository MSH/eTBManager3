package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.Medicine;
import org.msh.etbm.services.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msantos on 24/5/16.
 */
@Component
public class LaboratoryOptions implements FormRequestHandler<List<Item>> {

    public static final String CMD_NAME = "laboratories";

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
        List<Laboratory> labList = entityManager.createQuery("from Laboratory l " +
                                        "where l.workspace.id = :wId and l.active = :true " +
                                        "order by l.name")
                                    .setParameter("wId", userRequestService.getUserSession().getWorkspaceId())
                .setParameter("true", true)
                .getResultList();

        for (Laboratory l : labList) {
            options.add(new Item(l.getId(), l.getName()));
        }

        return options;
    }
}
