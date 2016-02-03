package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.options.OptionsManagerService;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.services.usersession.UserRequestService;
import org.msh.etbm.services.usersession.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserWsServiceImpl extends EntityServiceImpl<UserWorkspace, UserWsQueryParams> implements UserWsService {

    public static final String OPTIONS_USERVIEWS = "userViews";

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    OptionsManagerService optionsManagerService;

    @PersistenceContext
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        optionsManagerService.register("userViews", this);
    }

    /**
     * Return a list of users in a workspace context based on the given query params
     * @param params the query parameters to query the users
     * @return
     */
    @Override
    public QueryResult<SynchronizableItem> findMany(UserWsQueryParams params) {
        QueryBuilder<UserWorkspace> builder = queryBuilderFactory.createQueryBuilder(UserWorkspace.class, "a");

        // add profiles
        builder.addProfile(UserWsQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addDefaultProfile(UserWsQueryParams.PROFILE_DEFAULT, UserWsData.class);
        builder.addProfile(UserWsQueryParams.PROFILE_DETAILED, UserWsDetailedData.class);

        // add order by
        builder.addDefaultOrderByMap(UserWsQueryParams.ORDERBY_NAME, "u.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_UNIT, "u.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_ADMINUNIT, "adminUnit.name");

        builder.initialize(params);

        builder.setHqlJoin("join fetch a.user u join fetch a.unit u");

        QueryResult<SynchronizableItem> res = builder.createQueryResult();
        return res;
    }

    //@Override
    public List<Item> getOptions(Map<String, Object> params) {
        UserSession us = userRequestService.getUserSession();

        List<Item> options = new ArrayList<>();
        options.add(new Item<String>("workspace", us.getWorkspaceName()));

        // get the unit id
        String s = (String)params.get("unitId");
        if (s == null) {
            return options;
        }
        UUID unitId = UUID.fromString(s);

        // get the unit
        Unit unit = entityManager.find(Unit.class, unitId);
        List<AdministrativeUnit> lst = unit.getAddress().getAdminUnit().getParentsTreeList(true);

        for (AdministrativeUnit adm: lst) {
            options.add(new Item("A" + adm.getId(), adm.getName()));
        }
        return options;
    }

}
