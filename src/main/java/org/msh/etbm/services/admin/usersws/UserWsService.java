package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.services.admin.usersws.data.UserWsChangePwdFormData;
import org.msh.etbm.services.security.password.ChangePasswordResponse;

/**
 * Created by rmemoria on 3/2/16.
 */
public interface UserWsService extends EntityService<UserWsQueryParams> {
    /**
     * Changes the password of a selected user.
     * @param data contains the id and the new password of the user.
     * @return data for command log register
     */
    ChangePasswordResponse changePassword(UserWsChangePwdFormData data);

    /**
     * Sends an email to a selected user, so it can changes its own password throw a public form.
     * @param data contains the id and the new password of the user.
     * @return data for command log register
     */
    ChangePasswordResponse sendPwdResetLink(UserWsChangePwdFormData data);
}

