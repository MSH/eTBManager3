package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.unitview.UnitViewData;
import org.msh.etbm.services.cases.unitview.UnitViewService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST api to provide data of a unit view in the case management module
 *
 * Created by rmemoria on 3/6/16.
 */
@RestController
@RequestMapping("/api/cases")
@Authenticated(permissions = {Permissions.CASES})
public class UnitViewREST {

    @Autowired
    UnitViewService service;

    @RequestMapping(value = "/unit/{unitID}", method = RequestMethod.POST)
    public UnitViewData getUnitView(@PathVariable UUID unitID) {
        return service.getUnitView(unitID);
    }
}
