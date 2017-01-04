package org.msh.etbm.web.api.offline;

import org.msh.etbm.services.offline.client.data.ServerCredentialsData;
import org.msh.etbm.services.offline.client.data.ServerStatusResponse;
import org.msh.etbm.services.offline.client.init.ClientModeInitService;
import org.msh.etbm.services.offline.client.sync.ClientSyncFileGenerator;
import org.msh.etbm.services.offline.client.sync.ClientSyncService;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * REST API to handle Off-line mode synchronization operations
 * Created by Mauricio on 21/11/16.
 */
@RestController
@RequestMapping("/api/offline/client")
@Authenticated
public class ClientSyncREST {

    @Autowired
    ClientSyncService service;

    /**
     * Starts the sync progress and returns the current status of it
     * @param req
     * @return
     */
    @RequestMapping(value = "/sync/synchronize", method = RequestMethod.POST)
    public ServerStatusResponse initialize(@Valid @NotNull @RequestBody ServerCredentialsData req) {
        return service.synchronize(req);
    }

    /**
     * Returns the status of the initialization progress
     * @return
     */
    @RequestMapping(value = "/sync/status", method = RequestMethod.GET)
    public ServerStatusResponse initStatus() {
        return service.getStatus();
    }

}
