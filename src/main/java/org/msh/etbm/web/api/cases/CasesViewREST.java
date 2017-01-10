package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.view.CasesViewResponse;
import org.msh.etbm.services.cases.view.CasesViewService;
import org.msh.etbm.services.cases.view.PlaceData;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 17/6/16.
 */
@RestController
@RequestMapping("/api/cases")
@Authenticated(permissions = {Permissions.CASES})
public class CasesViewREST {

    @Autowired
    CasesViewService service;

    @Autowired
    UserRequestService userRequestService;

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public List<PlaceData> generateReport() {
        UUID workspaceId = userRequestService.getUserSession().getWorkspaceId();

        return service.generateWorkspaceView(workspaceId);
    }

    @RequestMapping(value = "/view/adminunit", method = RequestMethod.POST)
    public List<PlaceData> generateReport(@RequestParam UUID adminUnitId) {
        return service.generateAdminUnitView(adminUnitId);
    }

    @RequestMapping(value = "/view/places", method = RequestMethod.POST)
    public List<PlaceData> generateReportByAdminUnit(@RequestParam UUID parentId) {
        return service.generateAdminUnitView(parentId);
    }
}
