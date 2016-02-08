package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.usersession.UserRequestService;
import org.msh.etbm.services.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserWsServiceImpl extends EntityServiceImpl<UserWorkspace, UserWsQueryParams> implements UserWsService {

    public static final String CMD_NAME = "userViews";
    public static final String PARAM_UNITID = "unitId";

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    Messages messages;

    @Override
    protected void buildQuery(QueryBuilder<UserWorkspace> builder, UserWsQueryParams queryParams) {
        builder.setEntityAlias("a");

        // add profiles
        builder.addProfile(UserWsQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addDefaultProfile(UserWsQueryParams.PROFILE_DEFAULT, UserWsData.class);
        builder.addProfile(UserWsQueryParams.PROFILE_DETAILED, UserWsDetailedData.class);

        // add order by
        builder.addDefaultOrderByMap(UserWsQueryParams.ORDERBY_NAME, "u.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_UNIT, "u.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_ADMINUNIT, "adminUnit.name");

        builder.setHqlJoin("join fetch a.user u join fetch a.unit u");
    }


    /**
     * Provide list of options to the client side
     * @param req request to be executed
     * @return list of options to the user profile list
     */
    @Override
    public List<Item> execFormRequest(FormRequest req) {
        UserSession us = userRequestService.getUserSession();

        List<Item> options = new ArrayList<>();
        options.add(new Item<String>("workspace", us.getWorkspaceName()));

        // get the unit id
        String s = req.getStringParam(PARAM_UNITID);

        // no unit selected ?
        if (s == null) {
            return options;
        }
        UUID unitId = UUID.fromString(s);

        // get the unit
        Unit unit = entityManager.find(Unit.class, unitId);
        List<AdministrativeUnit> lst = unit.getAddress().getAdminUnit().getParentsTreeList(true);

        // include the administrative units
        for (AdministrativeUnit adm: lst) {
            options.add(new Item("A" + adm.getId(), adm.getCountryStructure().getName() + ": " + adm.getName()));
        }

        options.add(new Item("U" + unit.getId(), unit.getName()));

        options.add(new Item("SEL", messages.get(UserView.SELECTEDUNITS.getKey())));

        return options;
    }

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }

}
