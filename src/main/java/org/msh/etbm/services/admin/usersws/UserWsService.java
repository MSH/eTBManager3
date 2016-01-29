package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.UserWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserWsService extends EntityService<UserWorkspace> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

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
}
