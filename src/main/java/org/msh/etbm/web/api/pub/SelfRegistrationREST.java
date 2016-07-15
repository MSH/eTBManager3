package org.msh.etbm.web.api.pub;

import org.msh.etbm.services.pub.SelfRegistrationRequest;
import org.msh.etbm.services.pub.SelfRegistrationService;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Rest API to allow someone to self register himself
 * <p>
 * Created by rmemoria on 16/6/16.
 */
@RestController
@RequestMapping("/api/pub")
public class SelfRegistrationREST {

    @Autowired
    SelfRegistrationService service;

    @RequestMapping(value = "/selfreg", method = RequestMethod.POST)
    public StandardResult register(@RequestBody @Valid @NotNull SelfRegistrationRequest req) {
        service.register(req);

        return StandardResult.createSuccessResult();
    }
}
