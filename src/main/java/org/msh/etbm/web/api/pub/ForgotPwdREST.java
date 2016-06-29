package org.msh.etbm.web.api.pub;

import org.msh.etbm.services.pub.ForgotPwdService;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API to reset the user password when he forgot the one in use
 * <p>
 * Created by rmemoria on 13/6/16.
 */
@RestController
@RequestMapping("/api/pub")
public class ForgotPwdREST {

    @Autowired
    ForgotPwdService service;

    @RequestMapping(value = "/forgotpwd", method = RequestMethod.POST)
    public StandardResult requestNewPwd(@RequestParam(value = "id") String id) {
        String res = service.requestPasswordReset(id);

        return new StandardResult(null, null, res != null);
    }

}
