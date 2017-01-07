package org.msh.etbm.services.offline.client.sync;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.ParentServerFileSender;
import org.msh.etbm.services.offline.client.ParentServerRequestService;
import org.msh.etbm.services.offline.client.data.ServerCredentialsData;
import org.msh.etbm.services.offline.StatusResponse;
import org.msh.etbm.services.offline.fileimporter.FileImporter;
import org.msh.etbm.services.security.ForbiddenException;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * Created by Mauricio on 04/01/2017.
 */
@Service
public class ClientSyncService {

    @Autowired
    ParentServerRequestService request;

    @Autowired
    ParentServerFileSender fileSender;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    ClientSyncFileGenerator fileGenerator;

    @Autowired
    Messages messages;

    @Autowired
    FileImporter importer;

    UUID authToken;

    String syncToken;

    /**
     * If null initializing is not running
     */
    ClientSyncPhase phase = null;

    public StatusResponse synchronize(ServerCredentialsData data) {
        if (phase != null) {
            throw new SynchronizationException("Synchronization progress is already running");
        }

        phase = ClientSyncPhase.STARTING;

        // set login on credentials. Password was inputted on the request
        data.setUsername(userRequestService.getUserSession().getUserLoginName());

        // Login into remote server
        LoginResponse loginRes = request.post("/api/auth/login",
                null,
                data,
                null,
                LoginResponse.class);

        if (!loginRes.isSuccess()) {
            // throw a user friendly password
            throw new ForbiddenException();
        }

        authToken = loginRes.getAuthToken();

        UUID unitId = userRequestService.getUserSession().getUnitId();
        UUID workspaceId = userRequestService.getUserSession().getWorkspaceId();

        // starts file generating asynchronously and then calls sendFileToServer method
        phase = ClientSyncPhase.GENERATING_FILE;
        fileGenerator.generate(unitId, workspaceId, clientSyncFile -> sendFileToServer(clientSyncFile));

        return getStatus();
    }

    private void sendFileToServer(File clientSyncFile) {
        phase = ClientSyncPhase.SENDING_FILE;

        StatusResponse response = fileSender.sendFile("/api/offline/server/sync/start",
            authToken.toString(),
            clientSyncFile,
            StatusResponse.class);

        syncToken = response.getToken();

        checkServerSyncStatus();
    }

    private void checkServerSyncStatus() {
        try {
            StatusResponse response = request.get("/api/offline/server/sync/status/" + syncToken,
                    authToken.toString(),
                    null,
                    StatusResponse.class);

            switch (response.getId()) {
                case "WAITING_SERVER_FILE_IMPORTING":
                    // next step - download response file
                    request.downloadFile("/api/offline/server/sync/response/" + syncToken,
                            authToken.toString(),
                            downloadedFile -> importFile(downloadedFile));
                    break;
                case "IMPORTING_CLIENT_FILE":
                case "GENERATING_SERVER_FILE":
                case "WAITING_SERVER_FILE_DOWNLOAD":
                    // wait 700 ms
                    Thread.sleep(700);
                    // check again
                    checkServerSyncStatus();
                    break;
                default:
                    throw new SynchronizationException("Not expected status returned from server.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void importFile(File responseFile) {
        importer.importFile(responseFile, true, null, (importedFile, fileVersion) -> afterImporting(importedFile));
    }

    private void afterImporting(File importedFile) {
        // delete the file
        if (importedFile != null) {
            importedFile.delete();
        }

        // clear phase
        this.phase = null;

        // TODO: register commandlog
    }

    /**
     * Return the initialization status now
     * @return
     */
    public StatusResponse getStatus() {
        if (phase == null) {
            ClientSyncPhase notRunning = ClientSyncPhase.NOT_RUNNING;
            return new StatusResponse(notRunning.name(), messages.get(notRunning.getMessageKey()));
        }
        // TODO: improve this
        return new StatusResponse(phase.name(), messages.get(phase.getMessageKey()));
    }

    /**
     * Enum used to track the initialization progress
     */
    public enum ClientSyncPhase {
        NOT_RUNNING,
        STARTING,
        GENERATING_FILE,
        SENDING_FILE,
        RECEIVING_FILE,
        IMPORTING_FILE,
        FINISHING;

        String getMessageKey() {
            return "init.offinit.phase." + this.name();
        }
    }

}
