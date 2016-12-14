package org.msh.etbm.web.api.sync;

import org.apache.commons.compress.utils.IOUtils;
import org.msh.etbm.services.sync.server.SyncFileService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by rmemoria on 23/11/16.
 */
@RestController
@RequestMapping(path = "/api/sync")
@Authenticated
public class ServerSyncREST {

    @Autowired
    SyncFileService syncFileService;

    @Autowired
    UserRequestService userRequestService;

    @RequestMapping(path = "/inifile", method = RequestMethod.GET)
    public void downloadIniFile(HttpServletResponse resp) throws FileNotFoundException, IOException {
        UUID unitId = userRequestService.getUserSession().getUnitId();

        // generate the file content
        File file = syncFileService.generate(unitId, Optional.empty());

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
