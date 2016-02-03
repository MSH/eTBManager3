package org.msh.etbm.services.admin.userprofiles;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.options.OptionsManagerService;
import org.msh.etbm.commons.forms.options.OptionsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Options resolver for user profiles. Provide a list of options for user profiles selection in an UI
 *
 * Created by rmemoria on 3/2/16.
 */
@Component
public class UserProfileOptionsProvider implements OptionsProvider {

    @Autowired
    OptionsManagerService optionsManagerService;

    @Autowired
    UserProfileService userProfileService;

    @PostConstruct
    public void init() {
        optionsManagerService.register("userProfiles", this);
    }


    @Override
    public List<Item> getOptions(Map<String, Object> params) {
        UserProfileQueryParams qry = new UserProfileQueryParams();
        qry.setProfile(UserProfileQueryParams.PROFILE_ITEM);
        qry.setOrderBy(UserProfileQueryParams.ORDERBY_NAME);

        QueryResult res = userProfileService.findMany(qry);
        return res.getList();
    }
}
