package org.msh.etbm.services.admin.products;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
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
 * Created by rmemoria on 10/2/16.
 */
@Component
public class MedicineOptions implements FormRequestHandler<List<Item>> {

    public static final String CMD_NAME = "medicines";

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
        List<Medicine> medList = entityManager.createQuery("from Medicine m " +
                                        "where m.workspace.id = :wId and m.active = :true " +
                                        "order by m.name")
                                    .setParameter("wId", userRequestService.getUserSession().getWorkspaceId())
                .setParameter("true", true)
                .getResultList();

        for (Medicine m : medList) {
            options.add(new Item(m.getId(), m.getName()));
        }

        return options;
    }
}
