package org.msh.etbm.services.admin.userprofiles;


import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.db.entities.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the User profile service for CRUD operations
 *
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserProfileServiceImpl extends EntityServiceImpl<UserProfile, UserProfileQueryParams>
    implements UserProfileService {

    private static final String CMD_NAME = "profiles";

    @Override
    protected void buildQuery(QueryBuilder<UserProfile> builder, UserProfileQueryParams queryParams) {
        // add profiles
        builder.addProfile(UserProfileQueryParams.PROFILE_ITEM, SynchronizableItem.class);
        builder.addDefaultProfile(UserProfileQueryParams.PROFILE_DEFAULT, SynchronizableItem.class);
        builder.addProfile(UserProfileQueryParams.PROFILE_DETAILED, UserProfileDetailedData.class);

        // add order by
        builder.addDefaultOrderByMap(UserProfileQueryParams.ORDERBY_NAME, "name");
    }


    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }


    @Override
    public List<Item> execFormRequest(FormRequest req) {
        UserProfileQueryParams qry = new UserProfileQueryParams();
        qry.setProfile(UserProfileQueryParams.PROFILE_ITEM);
        QueryResult res = findMany(qry);
        return res.getList();
    }
}
