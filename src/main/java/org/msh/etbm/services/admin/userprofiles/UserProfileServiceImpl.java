package org.msh.etbm.services.admin.userprofiles;


import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.options.OptionsManagerService;
import org.msh.etbm.db.entities.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 26/1/16.
 */
@Service
public class UserProfileServiceImpl extends EntityServiceImpl<UserProfile, UserProfileQueryParams>
    implements UserProfileService {

    @Autowired
    OptionsManagerService optionsManagerService;

    /**
     * Register this component will provide options
     */
    @PostConstruct
    public void init() {
        optionsManagerService.register("profiles", this);
    }

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
    public List<Item> getOptions(Map<String, Object> params) {
        UserProfileQueryParams qry = new UserProfileQueryParams();
        qry.setProfile(UserProfileQueryParams.PROFILE_ITEM);
        QueryResult res = findMany(qry);
        return res.getList();
    }
}
