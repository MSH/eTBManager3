package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.usersws.data.UserViewData;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.services.session.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 10/2/16.
 */
@Component
public class UserViewOptions implements FormRequestHandler<List<Item>> {

    public static final String CMD_NAME = "userViews";
    public static final String PARAM_UNITID = "unitId";


    @Autowired
    Messages messages;

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public String getFormCommandName() {
        return "userViews";
    }

    @Override
    @Transactional
    public List<Item> execFormRequest(FormRequest req) {
        UserSession us = userRequestService.getUserSession();

        List<Item> options = new ArrayList<>();
        options.add(new Item<UserViewData>(new UserViewData(UserView.COUNTRY), us.getWorkspaceName()));

        // get the unit id
        UUID unitId = req.getIdParam(PARAM_UNITID);

        // no unit selected ?
        if (unitId == null) {
            return options;
        }

        // get the unit
        Unit unit = entityManager.find(Unit.class, unitId);
        AdministrativeUnit adminUnit = unit.getAddress().getAdminUnit();

        List<SynchronizableItem> lst = unit.getAddress().getAdminUnit().getParentsList(true);

        // include the administrative units
        for (SynchronizableItem adm : lst) {
            options.add(new Item<UserViewData>(
                            new UserViewData(UserView.ADMINUNIT, adm.getId()),
                            adm.getName())
            );
        }

        options.add(new Item<UserViewData>(new UserViewData(UserView.UNIT), unit.getName()));

        options.add(new Item(new UserViewData(UserView.SELECTEDUNITS), messages.get(UserView.SELECTEDUNITS.getKey())));

        return options;
    }
}
