package org.msh.etbm.services.offline.client.sync;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.ValidationException;
import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandStoreService;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.ParentServerFileSender;
import org.msh.etbm.services.offline.client.ParentServerRequestService;
import org.msh.etbm.services.offline.client.ServerCredentialsData;
import org.msh.etbm.services.offline.StatusResponse;
import org.msh.etbm.services.offline.fileimporter.FileImporter;
import org.msh.etbm.services.security.ForbiddenException;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
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

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CommandStoreService commandStoreService;

    /**
     * Credentials used to login on server
     */
    ServerCredentialsData credentials;

    /**
     * Auth token returned from server instance after logging in
     */
    UUID authToken;

    /**
     * Sync token returned from server instance after starting sync
     */
    String syncToken;

    /**
     * If null initializing is not running
     */
    ClientSyncPhase phase = null;

    /**
     * STEP 1
     * Starts synchronization with parent server
     * Checks the credentials
     * Starts client sync file generation
     * @param data the credentials (password) to login on parent server
     * @return the status of the sync process
     */
    public StatusResponse synchronize(ServerCredentialsData data) {
        // checks if sync is not running
        if (phase != null) {
            throw new SynchronizationException("Synchronization progress is already running");
        }

        // set login on credentials. Password was inputted on the request
        data.setUsername(userRequestService.getUserSession().getUserLoginName());
        data.setWorkspaceId(userRequestService.getUserSession().getWorkspaceId());

        try {
            // Login into remote server
            LoginResponse loginRes = request.post("/api/auth/login",
                    null,
                    data,
                    null,
                    LoginResponse.class);

            // checks if login was ok
            if (!loginRes.isSuccess()) {
                throw new ForbiddenException();
            }

            // stores the credentials and auth token to be used in other points
            authToken = loginRes.getAuthToken();
            credentials = data;

            UUID workspaceId = userRequestService.getUserSession().getWorkspaceId();

            // starts file generating asynchronously and then calls sendFileToServer method
            phase = ClientSyncPhase.GENERATING_FILE;
            // Async starts here
            fileGenerator.generate(workspaceId, clientSyncFile -> sendFileToServer(clientSyncFile));

            return getStatus();
        } catch (ForbiddenException e) {
            phase = null;
            throw new ValidationException(null, "sync.invalidpassword");
        } catch (ConnectException e) {
            phase = null;
            throw new ValidationException(null, "sync.connectionproblem");
        } catch (UnknownHostException e) {
            phase = null;
            throw new ValidationException(null, "sync.connectionproblem");
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }

    /**
     * STEP 2
     * After generating the sync file this method is called to send the generated file to server.
     * @param clientSyncFile the sync file generated
     */
    private void sendFileToServer(File clientSyncFile) {
        try {
            phase = ClientSyncPhase.SENDING_FILE;

            StatusResponse response = fileSender.sendFile("/api/offline/server/sync/start",
                    authToken.toString(),
                    clientSyncFile,
                    StatusResponse.class);

            syncToken = response.getToken();

            phase = ClientSyncPhase.WAITING_SERVER;
            checkServerSyncStatus();
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }

    /**
     * STEP 3
     * Keeps checking the server status while server is downloading and importing the client sync file.
     * After importing, server will wait this client instance to download the response file.
     */
    private void checkServerSyncStatus() {
        try {
            // checks server status
            StatusResponse response = request.get("/api/offline/server/sync/status/" + syncToken,
                    authToken.toString(),
                    null,
                    StatusResponse.class);

            switch (response.getId()) {
                case "IMPORTING_CLIENT_FILE":
                case "GENERATING_SERVER_FILE":
                    // server is still downloading or importing the client sync file or generating the response file
                    // wait 700 ms
                    Thread.sleep(700);
                    // check again
                    checkServerSyncStatus();
                    break;
                case "WAITING_SERVER_FILE_DOWNLOAD":
                    // server finished downloading and importing the client sync file, and had already generated the response file
                    // now client will download the response file
                    downloadServerResponseFile();
                    break;
                default:
                    throw new SynchronizationException("Not expected status returned from server.");
            }
        } catch (InterruptedException e) {
            phase = null;
            throw new SynchronizationException(e);
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }

    /**
     * STEP 4
     * DownloadS server sync file after server had already imported the client sync file.
     */
    private void downloadServerResponseFile() {
        try {
            phase = ClientSyncPhase.RECEIVING_RESPONSE_FILE;

            // download file
            request.downloadFile("/api/offline/server/sync/response/" + syncToken,
                    authToken.toString(), downloadedFile -> importFile(downloadedFile));
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }

    /**
     * STEP 5
     * Imports the server sync file.
     * @param responseFile downloaded server sync file
     */
    private void importFile(File responseFile) {
        try {
            phase = ClientSyncPhase.IMPORTING_RESPONSE_FILE;
            // import file
            importer.importFile(responseFile, true, null, (importedFile, fileVersion) -> afterImporting(importedFile));
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }

    /**
     * STEP 6 (the last step)
     * Called after importing server sync file.
     * Deletes the file clears the state of this component and register command log
     * @param importedFile the file that was imported
     */
    private void afterImporting(File importedFile) {
        // delete the file
        if (importedFile != null) {
            importedFile.delete();
        }

        // register commandlog
        Object[] o = (Object[]) entityManager.createQuery("select uw.workspace, uw.unit, uw.user from UserWorkspace uw where uw.user.login like :login")
                .setParameter("login", credentials.getUsername())
                .getSingleResult();

        CommandHistoryInput in = new CommandHistoryInput();
        in.setWorkspaceId(((Workspace)o[0]).getId());
        in.setUnitId(((Unit)o[1]).getId());
        in.setUserId(((User)o[2]).getId());
        in.setAction(CommandAction.EXEC);
        in.setType(CommandTypes.OFFLINE_CLIENTSYNC);

        commandStoreService.store(in);

        // notify server that sync finished
        try {
            request.get("/api/offline/server/sync/end/" + syncToken,
                    authToken.toString(),
                    null,
                    StandardResult.class);
        } catch (IOException e) {
            throw new SynchronizationException(e);
        } finally {
            // clear phase and aux info
            this.phase = null;
            this.authToken = null;
            this.syncToken = null;
        }
    }

    /**
     * Return the synchronization status
     * @return the status id and title
     */
    public StatusResponse getStatus() {
        if (phase == null) {
            ClientSyncPhase notRunning = ClientSyncPhase.NOT_RUNNING;
            return new StatusResponse(notRunning.name(), messages.get(notRunning.getMessageKey()));
        }

        return new StatusResponse(phase.name(), messages.get(phase.getMessageKey()));
    }

    /**
     * Enum used to track the synchronization process
     */
    public enum ClientSyncPhase {
        NOT_RUNNING,
        GENERATING_FILE,
        SENDING_FILE,
        WAITING_SERVER,
        RECEIVING_RESPONSE_FILE,
        IMPORTING_RESPONSE_FILE;

        String getMessageKey() {
            return "sync.client.phase." + this.name();
        }
    }

}
