package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.indicators.CaseIndicatorInitResponse;
import org.msh.etbm.services.cases.indicators.CaseIndicatorRequest;
import org.msh.etbm.services.cases.indicators.CaseIndicatorResponse;
import org.msh.etbm.services.cases.indicators.CaseIndicatorsService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * REST API for case indicator generation
 *
 * Created by rmemoria on 5/10/16.
 */
@RestController
@RequestMapping("/api/cases/indicator")
@Authenticated(permissions = {Permissions.CASES})
public class CaseIndicatorsREST {

    @Autowired
    CaseIndicatorsService service;

    @RequestMapping(value = "/exec", method = RequestMethod.POST)
    public CaseIndicatorResponse generateIndicator(@RequestBody @Valid @NotNull CaseIndicatorRequest req) {
        return service.execute(req);
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public CaseIndicatorInitResponse getIniiData() {
        return service.getInitData();
    }
}
