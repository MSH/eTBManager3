package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

/**
 * Parameters to query workspaces
 *
 * Created by rmemoria on 12/11/15.
 */
public class WorkspaceQueryParams extends EntityQueryParams {
    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";
    public static final String ORDERBY_NAME = "name";
}
