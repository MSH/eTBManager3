package org.msh.etbm.rest.authentication;

import org.msh.etbm.rest.exceptions.UnauthorizedException;
import org.msh.etbm.services.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API to authenticate the user by its user name and password
 * Created by rmemoria on 21/8/15.
 */
@RestController
@RequestMapping("/api")
public class AuthenticatorRest {

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginForm frm) {
        String authToken = authenticationService.authenticate(frm.getUsername(), frm.getPassword(), frm.getWorkspaceId());

        if (authToken == null) {
            throw new UnauthorizedException("Invalid username or password");
        }

        return authToken;
    }
}
