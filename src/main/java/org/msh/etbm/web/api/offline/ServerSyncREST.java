package org.msh.etbm.web.api.offline;

import org.apache.commons.compress.utils.IOUtils;
import org.msh.etbm.services.offline.StatusResponse;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.server.ServerSyncService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.InstanceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by msantos on 2/01/17.
 */
@RestController
@RequestMapping(path = "/api/offline/server")
@Authenticated(instanceType = InstanceType.SERVER_MODE)
public class ServerSyncREST {

    @Autowired
    ServerSyncService serverSyncService;

    @RequestMapping(path = "/sync/start", method = RequestMethod.POST)
    public StatusResponse startSync(@RequestParam("file") MultipartFile multiFile) {
        try {
            File file = File.createTempFile("clientsyncfile", "etbm");
            multiFile.transferTo(file);

            return serverSyncService.startSync(file);
        } catch (IOException e) {
            throw  new SynchronizationException("Error while downloading file.");
        }
    }

    @RequestMapping(value = "/sync/status/{token}", method = RequestMethod.GET)
    public StatusResponse getStatus(@PathVariable String token) {
        return serverSyncService.getStatus(token);
    }

    @RequestMapping(path = "/sync/response/{token}", method = RequestMethod.GET)
    public void downloadResponseFile(@PathVariable String token, HttpServletResponse resp) throws FileNotFoundException, IOException {
        // get the file
        File file = serverSyncService.getResponseFile(token);

        // generate the file name
        String filename = token + ".etbm";
        filename = filename.replaceAll("[^a-zA-Z0-9.]", "_");

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");

        // send data to the client
        InputStream in = new FileInputStream(file);

        IOUtils.copy(in, resp.getOutputStream());
        resp.flushBuffer();
    }

    @RequestMapping(value = "/sync/end/{token}", method = RequestMethod.GET)
    public StandardResult endSync(@PathVariable String token) {
        serverSyncService.endSync(token);

        return StandardResult.createSuccessResult();
    }
}
