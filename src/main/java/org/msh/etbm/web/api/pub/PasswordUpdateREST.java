package org.msh.etbm.web.api.pub;

import org.msh.etbm.services.security.password.PasswordUpdateRequest;
import org.msh.etbm.services.security.password.PasswordUpdateService;
import org.msh.etbm.services.pub.PwdResetTokenResponse;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * REST API to update a user password using a password request token, usually
 * received by e-mail under a previous request to reset the password
 * <p>
 * Created by rmemoria on 16/6/16.
 */
@RestController
@RequestMapping("/api/pub")
public class PasswordUpdateREST {

    @Autowired
    PasswordUpdateService service;

    @RequestMapping(value = "/pwdtokeninfo/{token}", method = RequestMethod.POST)
    public PwdResetTokenResponse getUserInfoByToken(@PathVariable String token) {
        return service.getUserInfoByPasswordResetToken(token);
    }

    @RequestMapping(value = "/updatepwd", method = RequestMethod.POST)
    public StandardResult updatePassword(@RequestBody @Valid @NotNull PasswordUpdateRequest req) {
        service.updatePassword(req);

        return new StandardResult(null, null, true);
    }
}
