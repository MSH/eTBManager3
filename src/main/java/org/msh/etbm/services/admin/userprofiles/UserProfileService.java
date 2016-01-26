package org.msh.etbm.services.admin.userprofiles;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserProfileService extends EntityService<UserProfile> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Search for user profiles by the given query params
     * @param params
     * @return
     */
    public QueryResult<SynchronizableItem> findMany(UserProfileQueryParams params) {
        QueryBuilder<UserProfile> builder = queryBuilderFactory.createQueryBuilder(UserProfile.class, "a");

        // add profiles
        builder.addProfile(UserProfileQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addDefaultProfile(UserProfileQueryParams.PROFILE_DEFAULT, SynchronizableItem.class);
        builder.addProfile(UserProfileQueryParams.PROFILE_DETAILED, UserProfileDetailedData.class);

        // add order by
        builder.addDefaultOrderByMap(UserProfileQueryParams.ORDERBY_NAME, "name");

        builder.initialize(params);

        QueryResult<SynchronizableItem> res = builder.createQueryResult();

        return res;
    }
}
