package org.msh.etbm.services.offline.client.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.*;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.services.offline.client.ParentServerRequestService;
import org.msh.etbm.services.offline.fileimporter.FileImporter;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.data.ServerCredentialsData;
import org.msh.etbm.services.offline.StatusResponse;
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
 * Service to initialize a client mode instance.
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

    private ServerCredentialsData credentials;

    /**
     * Returns all workspaces attached to the user that matches with credentials on param.
     * @param data
     * @return
     */
    public List<WorkspaceInfo> findWorkspaces(ServerCredentialsData data) {
        if (isInitialized()) {
            throw new EntityValidationException(data, null, null, "init.offinit.error3");
        }

        try {
            // todo: COMENTADO TEMPORARIAMENTE
            //String serverAddress = checkServerAddress(data.getParentServerUrl());
            String serverAddress = "http://localhost:8081";

            TypeFactory typeFactory = new ObjectMapper().getTypeFactory();

            List<WorkspaceInfo> response = request.post(serverAddress,
                    "/api/auth/workspaces",
                    null,
                    data,
                    typeFactory.constructCollectionType(List.class, WorkspaceInfo.class),
                    null);

            return response;

        } catch (MalformedURLException e) {
            // todo
        } catch (UnknownHostException e) {
            // todo
        } catch (IOException e) {
            throw new SynchronizationException(e);
        }

        return null;
    }

    /**
     * Download and import init file.
     * @param data
     */
    public StatusResponse initialize(ServerCredentialsData data) {
        if (isInitialized()) {
            throw new EntityValidationException(data, null, null, "init.offinit.error3");
        }

        if (phase != null) {
            throw new SynchronizationException("Initialization progress is already running");
        }

        try {
            // todo: COMENTADO TEMPORARIAMENTE
            //String serverAddress = checkServerAddress(data.getParentServerUrl());
            String serverAddress = "http://localhost:8081";

            // Login into remote server
            LoginResponse loginRes = request.post(serverAddress,
                    "/api/auth/login",
                    null,
                    data,
                    null,
                    LoginResponse.class);

            // Asynchronously download and import file
            phase = ClientModeInitPhase.DOWNLOADING_FILE;
            credentials = data;

            // Async starts here
            request.downloadFile(serverAddress,
                    "/api/offline/server/inifile",
                    loginRes.getAuthToken().toString(),
                    downloadedFile -> importFile(downloadedFile, serverAddress));

        } catch (UnknownHostException e) {
            phase = null;
            // todo
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }

        return getStatus();
    }


    /**
     * Asynchronously imports the initialization file
     * @param file
     * @param serverAddress
     */
    private void importFile(File file, String serverAddress) {
        phase = ClientModeInitPhase.IMPORTING_FILE;

        try {
            importer.importFile(file, true, serverAddress, (importedFile, fileVersion) -> afterImporting(importedFile));
        } catch (IOException e) {
            phase = null;
            throw new SynchronizationException(e);
        }
    }

    /**
     * Called after importing file
     * @param importedFile
     */
    private void afterImporting(File importedFile) {
        // delete the file
        if (importedFile != null) {
            importedFile.delete();
        }

        // clear phase
        this.phase = null;

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
    }

    /**
     * Checks if database is already initialized
     * @return
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
        String server = url;
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
        return server;
    }

    /**
     * Return the initialization status now
     * @return
     */
    public StatusResponse getStatus() {
        if (phase == null) {
            return getStatusResponse(ClientModeInitPhase.NOT_RUNNING);
        }

        if (phase.equals(ClientModeInitPhase.IMPORTING_FILE)) {
            if (importer.getPhase() == null) {
                return new StatusResponse(ClientModeInitPhase.IMPORTING_FILE.name(), messages.get("init.offinit.phase.IMPORTING_TABLES"));
            }

            String msg = messages.get("init.offinit.phase." + importer.getPhase().name());
            String complement = importer.getImportingTable() != null ? ": " + importer.getImportingTable() : "";

            return new StatusResponse(importer.getPhase().name(), msg + complement);
        }

        return getStatusResponse(phase);
    }

    /**
     * Builds ServerStatusResponse to response status service.
     * @param phase
     * @return
     */
    private StatusResponse getStatusResponse(ClientModeInitPhase phase) {
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
