package org.msh.etbm.web.api.sync;

import org.apache.commons.compress.utils.IOUtils;
import org.msh.etbm.services.offline.client.init.ClientModeInitService;
import org.msh.etbm.services.offline.client.data.ServerCredentialsData;
import org.msh.etbm.services.offline.client.sync.ClientSyncFileGenerator;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.services.offline.client.data.ServerStatusResponse;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST API to handle Off-line mode initialization and synchronization operations
 * Created by Mauricio on 21/11/16.
 */
@RestController
@RequestMapping("/api/offline")
public class ClientSyncREST {

    @Autowired
    ClientModeInitService service;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    ClientSyncFileGenerator generator;

    /**
     * Returns the workspaces available for the user/password sent on request
     * @param req
     * @return
     */
    @RequestMapping(value = "/init/workspaces", method = RequestMethod.POST)
    public StandardResult findWorkspaces(@Valid @NotNull @RequestBody ServerCredentialsData req) {

        List<WorkspaceInfo> res = service.findWorkspaces(req);

        return new StandardResult(res, null, true);
    }

    /**
     * Starts the initialization progress and returns the current status of it
     * @param req
     * @return
     */
    @RequestMapping(value = "/init/initialize", method = RequestMethod.POST)
    public ServerStatusResponse initialize(@Valid @NotNull @RequestBody ServerCredentialsData req) {
        return service.initialize(req);
    }

    /**
     * Returns the status of the initialization progress
     * @return
     */
    @RequestMapping(value = "/init/status", method = RequestMethod.GET)
    public ServerStatusResponse status() {
        return service.getStatus();
    }

    //TODO: remove from here
    @RequestMapping(path = "/syncfile", method = RequestMethod.GET)
    @Authenticated
    public void downloadSyncFile(HttpServletResponse resp) throws FileNotFoundException, IOException {
        UUID unitId = userRequestService.getUserSession().getUnitId();

        // generate the file content
        File file = generator.generate(unitId);

        // generate the file name
        String filename = userRequestService.getUserSession().getWorkspaceName() + ".etbm";
        filename = filename.replaceAll("[^a-zA-Z0-9.]", "_");

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");

        // send data to the client
        InputStream in = new FileInputStream(file);

        IOUtils.copy(in, resp.getOutputStream());
        resp.flushBuffer();
    }
}
