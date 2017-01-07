package org.msh.etbm.services.offline.client.sync;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.ParentServerFileSender;
import org.msh.etbm.services.offline.client.ParentServerRequestService;
import org.msh.etbm.services.offline.client.data.ServerCredentialsData;
import org.msh.etbm.services.offline.StatusResponse;
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

    UUID authToken;

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

        StandardResult response = fileSender.sendFile("/api/offline/server/sync/startsync",
            authToken.toString(),
            clientSyncFile,
            StandardResult.class);

        waitServerFinishImporting("syncToken");

        // todo: remove this mock process
        try {
            Thread.sleep(2000);
            phase = ClientSyncPhase.RECEIVING_FILE;
            Thread.sleep(2000);
            phase = ClientSyncPhase.IMPORTING_FILE;
            Thread.sleep(2000);
            phase = ClientSyncPhase.FINISHING;
            Thread.sleep(1000);
            phase = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitServerFinishImporting(String syncToken) {
        try {
            // get server sync status for this client

            if (true) { // server is DOWNLOADING, IMPORTING OR GENERATING FILE
                // update this.phase (?)

                // wait 700 ms
                Thread.sleep(700);
                // check again
                waitServerFinishImporting(syncToken);
            } else if (false) { // server finished generating response file (is waiting file download)
                // download file
                // import file
                // end sync
            } else {
                throw new SynchronizationException("Not expected status returned from server.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
