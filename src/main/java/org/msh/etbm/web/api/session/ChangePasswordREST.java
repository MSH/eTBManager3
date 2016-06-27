package org.msh.etbm.web.api.session;

import org.msh.etbm.services.session.changepassword.ChangePasswordFormData;
import org.msh.etbm.services.session.changepassword.ChangePasswordService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by rmemoria on 15/5/16.
 */
@RestController
@RequestMapping(path = "/api/sys/changepassword")
@Authenticated
public class ChangePasswordREST {

    @Autowired
    ChangePasswordService changePasswordService;

    @RequestMapping(method = RequestMethod.POST)
    public void updateUserSettings(@RequestBody @Valid ChangePasswordFormData data) {
        changePasswordService.changePassword(data);
    }
}
