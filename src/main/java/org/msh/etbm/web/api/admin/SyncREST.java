package org.msh.etbm.web.api.admin;

import org.msh.etbm.services.sync.offline.ClientModeInitService;
import org.msh.etbm.services.sync.offline.ServerCredentialsData;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * REST API to handle Off-line mode initialization and synchronization operations
 * Created by Mauricio on 21/11/16.
 */
@RestController
@RequestMapping("/api/offline")
public class SyncREST {

    @Autowired
    ClientModeInitService service;

    @RequestMapping(value = "/init/workspaces", method = RequestMethod.POST)
    public StandardResult findWorkspaces(@Valid @NotNull @RequestBody ServerCredentialsData req) {

        List<WorkspaceInfo> res = service.findWorkspaces(req);

        return new StandardResult(res, null, true);
    }

    @RequestMapping(value = "/init/initialize", method = RequestMethod.POST)
    public StandardResult initialize(@Valid @NotNull @RequestBody ServerCredentialsData req) {

        service.initialize(req);

        return StandardResult.createSuccessResult();
    }
}
