package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.db.entities.UserWorkspace;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

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

    @Override
    protected void beforeSave(EntityDAO<UserWorkspace> dao) {
        UserWorkspace uw = dao.getEntity();

        if (uw.getAdminUnit() == null && uw.getUnit() != null) {
            uw.setAdminUnit(uw.getUnit().getAddress().getAdminUnit());
        }

        // check if there is any validation error
        if (!dao.validate()) {
            return;
        }

        // save the user
        getEntityManager().persist(dao.getEntity().getUser());
    }
}
