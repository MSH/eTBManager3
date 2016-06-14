package org.msh.etbm.web.api.pub;

import org.msh.etbm.services.pub.ForgotPwdService;
import org.msh.etbm.services.pub.PwdResetTokenResponse;
import org.msh.etbm.services.pub.PwdUpdateRequest;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * REST API to reset the user password when he forgot the one in use
 *
 * Created by rmemoria on 13/6/16.
 */
@RestController
@RequestMapping("/api/pub")
public class ForgotPwdREST {

    @Autowired
    ForgotPwdService service;

    @RequestMapping(value = "/forgotpwd", method = RequestMethod.POST)
    public StandardResult requestNewPwd(@RequestParam(value = "id") String id) {
        String res = service.initPasswordChange(id);

        return new StandardResult(null, null, res != null);
    }


    @RequestMapping(value = "/pwdresetinfo/{token}", method = RequestMethod.POST)
    public PwdResetTokenResponse getUserInfoByToken(@PathVariable String token) {
        return service.getUserInfoByPasswordResetToken(token);
    }

    @RequestMapping(value = "/updatepwd", method = RequestMethod.POST)
    public StandardResult updatePassword(@RequestBody @Valid @NotNull PwdUpdateRequest req) {
        service.updatePassword(req);

        return new StandardResult(null, null, true);
    }
}
