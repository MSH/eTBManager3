package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.services.admin.usersws.data.UserWsChangePwdFormData;

/**
 * Created by rmemoria on 3/2/16.
 */
public interface UserWsService extends EntityService<UserWsQueryParams> {
    public Diffs changePassword(UserWsChangePwdFormData data);
}

