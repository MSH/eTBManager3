package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.UserWorkspace;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link UserWsService} to handle CRUD operations for User workspace
 *
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserWsServiceImpl extends EntityServiceImpl<UserWorkspace, UserWsQueryParams> implements UserWsService {

    @Override
    protected void buildQuery(QueryBuilder<UserWorkspace> builder, UserWsQueryParams queryParams) {
        builder.setEntityAlias("a");

        // add profiles
        builder.addProfile(UserWsQueryParams.PROFILE_ITEM, UserWsItemData.class);
        builder.addDefaultProfile(UserWsQueryParams.PROFILE_DEFAULT, UserWsData.class);
        builder.addProfile(UserWsQueryParams.PROFILE_DETAILED, UserWsDetailedData.class);

        // add order by
        builder.addDefaultOrderByMap(UserWsQueryParams.ORDERBY_NAME, "u.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_UNIT, "un.name");
        builder.addOrderByMap(UserWsQueryParams.ORDERBY_ADMINUNIT, "adminUnit.name");

        builder.setHqlJoin("join fetch a.user u join fetch a.unit un");
    }

}
