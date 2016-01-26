package org.msh.etbm.services.admin.userprofiles;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

/**
 * Created by rmemoria on 26/1/16.
 */
public class UserProfileQueryParams extends EntityQueryParams {
    // available profiles
    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";

    public static final String ORDERBY_NAME = "name";
}
