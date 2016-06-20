package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.workspace.PlaceData;
import org.msh.etbm.services.cases.workspace.WorkspaceViewResponse;
import org.msh.etbm.services.cases.workspace.WorkspaceViewService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 17/6/16.
 */
@RestController
@RequestMapping("/api/cases")
@Authenticated(permissions = {Permissions.CASES})
public class WorkspaceViewREST {

    @Autowired
    WorkspaceViewService service;

    @RequestMapping(value = "/workspace", method = RequestMethod.POST)
    public WorkspaceViewResponse generateReport() {
        return service.generateView();
    }

    @RequestMapping(value = "/workspace/places", method = RequestMethod.POST)
    public List<PlaceData> generateReportByAdminUnit(@RequestParam UUID id) {
        return service.generateAdminUnitView(id);
    }
}
