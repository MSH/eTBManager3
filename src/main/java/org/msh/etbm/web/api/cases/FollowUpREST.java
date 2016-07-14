package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.followup.FollowUpService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by rmemoria on 23/5/16.
 */
@RestController
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES})
public class FollowUpREST {

    @Autowired
    FollowUpService service;

    @RequestMapping(value = "/followups/{caseId}", method = RequestMethod.GET)
    @Authenticated
    public QueryResult get(@PathVariable UUID caseId) {
        return service.getData(caseId);
    }

}
