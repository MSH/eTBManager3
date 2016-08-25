package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 25/8/16.
 */
@Component
public class CountryStructureOptions implements FormRequestHandler<List<Item>> {

    public static final String CMD_NAME = "countrystructures";

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
        List<CountryStructure> list = entityManager.createQuery("from CountryStructure c " +
                "where c.workspace.id = :wId " +
                "order by c.level")
                .setParameter("wId", userRequestService.getUserSession().getWorkspaceId())
                .getResultList();

        for (CountryStructure c : list) {
            options.add(new Item(c.getLevel(), c.getName()));
        }

        return options;
    }
}
