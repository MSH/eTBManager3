package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.casemove.CaseMoveRequest;
import org.msh.etbm.services.cases.casemove.CaseMoveService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Mauricio on 12/9/16.
 */
@RestController
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES_TRANSFER})
public class CaseMoveREST {

    @Autowired
    CaseMoveService service;

    @RequestMapping(value = "/transferout", method = RequestMethod.POST)
    public StandardResult transferOut(@Valid @NotNull @RequestBody CaseMoveRequest req) {
        service.transferOut(req);
        return StandardResult.createSuccessResult();
    }

    @RequestMapping(value = "/undotransferout/{caseId}", method = RequestMethod.GET)
    public StandardResult undoTransferOut(@PathVariable UUID caseId) {
        service.rollbackTransferOut(caseId);
        return StandardResult.createSuccessResult();
    }

    @RequestMapping(value = "/transferin", method = RequestMethod.POST)
    public StandardResult transferIn(@Valid @NotNull @RequestBody CaseMoveRequest req) {
        service.transferIn(req);
        return StandardResult.createSuccessResult();
    }

}
