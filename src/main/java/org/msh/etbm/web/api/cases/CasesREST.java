package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.cases.CaseDetailedData;
import org.msh.etbm.services.cases.cases.CaseService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES})
public class CasesREST {

    @Autowired
    CaseService service;

    @RequestMapping(value = "/case/{id}", method = RequestMethod.GET)
    public CaseDetailedData get(@PathVariable UUID id) {
        return service.findOne(id, CaseDetailedData.class);
    }

    @RequestMapping(value = "/case/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }
}
