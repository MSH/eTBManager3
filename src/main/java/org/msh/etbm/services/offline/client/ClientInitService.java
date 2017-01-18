package org.msh.etbm.services.offline.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.ValidationException;
import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandStoreService;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.offline.StatusResponse;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.fileimporter.FileImporter;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.web.api.authentication.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Service that initializes a client mode instance.
 * Created by Mauricio on 21/11/2016.
 */
@Service
public class ClientInitService {

    @Autowired
    ParentServerRequestService request;

    @Autowired
    FileImporter importer;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CommandStoreService commandStoreService;

    @Autowired
    Messages messages;

    /**
     * If null initializing is not running
     */
    private ClientModeInitPhase phase;

    /**
     * Stores the credentials used to login on server until the end of initialization
     */
    private ServerCredentialsData credentials;

    /**
     * @param data
     * @return Returns all workspaces attached to the user that matches with credentials on param.
     */
    public List<WorkspaceInfo> findWorkspaces(ServerCredentialsData data) {
        // checks if client database is already initialized
        if (isInitialized()) {
            throw new EntityValidationException(data, null, null, "init.offinit.error3");
        }

        try {
            String serverAddress = checkServerAddress(data.getParentServerUrl());

            TypeFactory typeFactory = new ObjectMapper().getTypeFactory();

            // request server expecting the list of workspaces
            List<WorkspaceInfo> response = request.post(serverAddress,
                    "/api/auth/workspaces",
                    null,
                    data,
                    typeFactory.constructCollectionType(List.class, WorkspaceInfo.class),
                    null);

            return response;

        } catch (MalformedURLException e) {
            throw new ValidationException(null, "init.offinit.urlnotfound");
        } catch (UnknownHostException e) {
            throw new ValidationException(null, "init.offinit.urlnotfound");
        } catch (IOException e) {
            throw new SynchronizationException(e);
        }
    }

    /**
     * Download and import init file.t
     * @param data
     * @return the status of initialize process
     */
    public StatusResponse initialize(ServerCredentialsData data) {
        // checks if client database is already initialized
        if (isInitialized()) {
            throw new EntityValidationException(data, null, null, "init.offinit.error3");
        }

        // checks if initialization is already running
        if (phase != null) {
            throw new SynchronizationException("Initialization progress is already running");
        }

        // start initialization
        try {
            String serverAddress = checkServerAddress(data.getParentServerUrl());

            // Login into remote server
            // As the workspace list was requested before, it is not expected any connection error here
            LoginResponse loginRes = request.post(serverAddress,
                    "/api/auth/login",
                    null,
                    data,
                    null,
                    LoginResponse.class);

            // Download and import file
            phase = ClientModeInitPhase.DOWNLOADING_FILE;
            credentials = data;
            data.setParentServerUrl(serverAddress);

            // Async starts here
            request.downloadFile(serverAddress,
                    "/api/offline/server/inifile",
                    loginRes.getAuthToken().toString(), downloadedFile -> importFile(downloadedFile));

            return getStatus();
        } catch (UnknownHostException e) {
            phase = null;
            throw new ValidationException(null, "init.offinit.urlnotfound");
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }


    /**
     * Asynchronously imports the initialization file
     * @param file the downloaded file
     */
    private void importFile(File file) {
        phase = ClientModeInitPhase.IMPORTING_FILE;

        try {
            // start importing
            importer.importFile(file, true, credentials.getParentServerUrl(), (importedFile, fileVersion) -> afterImporting(importedFile));
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }

    /**
     * Called after importing file to end the initialization process, registering command log.
     * @param importedFile
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
        in.setType(CommandTypes.OFFLINE_CLIENTINIT);

        commandStoreService.store(in);

        // clear phase
        this.phase = null;
        this.credentials = null;
    }

    /**
     * Checks if database is already initialized
     * @return true if database is already initialized
     */
    private boolean isInitialized() {
        List<Workspace> ws = entityManager.createQuery("from Workspace").getResultList();

        if (ws == null || ws.size() < 1) {
            return false;
        }

        return true;
    }

    /**
     * Check if the server address is incomplete. Append the name 'etbm3'
     * at the end of address and the protocol 'http://', if missing
     *
     * @param url is the address of eTB Manager web version
     * @return the address with complements
     */
    private String checkServerAddress(String url) {
        // TODO: temp comment
        return url;
        /*String server = url;
        // try to fill gaps in the composition of the server address
        if (!server.startsWith("http")) {
            server = "http://" + server;
        }

        if (!server.endsWith("etbm3") && !server.endsWith("etbm3/")) {
            if ((!server.endsWith("/")) || (!server.endsWith("\\"))) {
                server += "/";
            }
            server += "etbm3";
        }
        return server;*/
    }

    /**
     * Return the initialization status
     * @return Data object containing the phase ID and phase Title
     */
    public StatusResponse getStatus() {
        if (phase == null) {
            return new StatusResponse(ClientModeInitPhase.NOT_RUNNING.name(), messages.get(ClientModeInitPhase.NOT_RUNNING.getMessageKey()));
        }

        if (phase.equals(ClientModeInitPhase.IMPORTING_FILE)) {
            if (importer.getPhase() == null) {
                return new StatusResponse(ClientModeInitPhase.IMPORTING_FILE.name(), messages.get("init.offinit.phase.IMPORTING_TABLES"));
            }

            String msg = messages.get("init.offinit.phase." + importer.getPhase().name());
            String complement = importer.getImportingTable() != null ? ": " + importer.getImportingTable() : "";

            return new StatusResponse(importer.getPhase().name(), msg + complement);
        }

        return new StatusResponse(phase.name(), messages.get(phase.getMessageKey()));
    }

    /**
     * Enum used to track the initialization progress
     */
    public enum ClientModeInitPhase {
        DOWNLOADING_FILE,
        IMPORTING_FILE,
        NOT_RUNNING;

        String getMessageKey() {
            return "init.offinit.phase." + this.name();
        }
    }
}
