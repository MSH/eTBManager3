package org.msh.etbm.services.offline.client.sync;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.ParentServerRequestService;
import org.msh.etbm.services.offline.client.data.ServerCredentialsData;
import org.msh.etbm.services.offline.client.data.ServerStatusResponse;
import org.msh.etbm.services.security.ForbiddenException;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.authentication.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Mauricio on 04/01/2017.
 */
@Service
public class ClientSyncService {

    @Autowired
    ParentServerRequestService request;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    ClientSyncFileGenerator fileGenerator;

    @Autowired
    Messages messages;

    /**
     * If null initializing is not running
     */
    ClientModeSyncPhase phase = null;

    public ServerStatusResponse synchronize(ServerCredentialsData data) {
        if (phase != null) {
            throw new SynchronizationException("Synchronization progress is already running");
        }

        phase = ClientModeSyncPhase.STARTING;

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

        // starts file generating asynchronously and then calls sendFileToServer method
        phase = ClientModeSyncPhase.GENERATING_FILE;
        fileGenerator.generate(userRequestService.getUserSession().getUnitId(),
            generatedFile -> sendFileToServer(generatedFile));

        return getStatus();
    }

    private void sendFileToServer(File generatedFile) {
        phase = ClientModeSyncPhase.SENDING_FILE;

        try {
            Thread.sleep(2000);
            phase = ClientModeSyncPhase.RECEIVING_FILE;
            Thread.sleep(2000);
            phase = ClientModeSyncPhase.IMPORTING_FILE;
            Thread.sleep(2000);
            phase = ClientModeSyncPhase.FINISHING;
            Thread.sleep(1000);
            phase = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the initialization status now
     * @return
     */
    public ServerStatusResponse getStatus() {
        if (phase == null) {
            ClientModeSyncPhase notRunning = ClientModeSyncPhase.NOT_RUNNING;
            return new ServerStatusResponse(notRunning.name(), messages.get(notRunning.getMessageKey()));
        }
        // TODO: improve this
        return new ServerStatusResponse(phase.name(), messages.get(phase.getMessageKey()));
    }

    /**
     * Enum used to track the initialization progress
     */
    public enum ClientModeSyncPhase {
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
