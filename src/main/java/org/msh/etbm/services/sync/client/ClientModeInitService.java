package org.msh.etbm.services.sync.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandStoreService;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.services.sync.SynchronizationException;
import org.msh.etbm.services.sync.client.data.ServerCredentialsData;
import org.msh.etbm.services.sync.client.data.ServerStatusResponse;
import org.msh.etbm.web.api.authentication.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.List;

/**
 * Service to initialize a client mode instance.
 * Created by Mauricio on 21/11/2016.
 */
@Service
public class ClientModeInitService {

    @Autowired
    ParentServerRequestService request;

    @Autowired
    SyncFileImporter importer;

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
     * Returns all workspaces attached to the user that matches with credentials on param.
     * @param data
     * @return
     */
    public List<WorkspaceInfo> findWorkspaces(ServerCredentialsData data) {
        if (isInitialized()) {
            throw new EntityValidationException(data, null, null, "init.offinit.error3");
        }

        String serverAddress = checkServerAddress(data.getParentServerUrl());

        TypeFactory typeFactory = new ObjectMapper().getTypeFactory();

        List<WorkspaceInfo> response = request.post(serverAddress,
                "/api/auth/workspaces",
                null,
                data,
                typeFactory.constructCollectionType(List.class, WorkspaceInfo.class),
                null);

        return response;
    }

    /**
     * Download and import init file.
     * @param data
     */
    public ServerStatusResponse initialize(ServerCredentialsData data) {
        if (isInitialized()) {
            throw new EntityValidationException(data, null, null, "init.offinit.error3");
        }

        if (phase != null) {
            throw new SynchronizationException("Initialization progress is already running");
        }

        phase = ClientModeInitPhase.STARTING;

        String serverAddress = checkServerAddress(data.getParentServerUrl());

        // Login into remote server
        LoginResponse loginRes = request.post(serverAddress,
                "/api/auth/login",
                null,
                data,
                null,
                LoginResponse.class);

        // Asynchronously download and import file
        phase = ClientModeInitPhase.DOWNLOADING_FILE;
        request.downloadFile(serverAddress,
                "/api/sync/inifile/",
                loginRes.getAuthToken().toString(),
            downloadedFile -> importFile(data, downloadedFile, serverAddress));

        return getStatus();
    }


    /**
     * Asynchronously imports the initialization file
     * @param file
     * @param serverAddress
     */
    private void importFile(ServerCredentialsData data, File file, String serverAddress) {
        phase = ClientModeInitPhase.IMPORTING_FILE;

        importer.importFile(file, true, serverAddress, true, importedFile -> afterImporting(data, importedFile));
    }

    /**
     * Called after importing file
     * @param importedFile
     */
    private void afterImporting(ServerCredentialsData data, File importedFile) {
        // delete the file
        if (importedFile != null) {
            importedFile.delete();
        }

        // clear phase
        this.phase = null;

        // register commandlog
        Object[] o = (Object[]) entityManager.createQuery("select uw.workspace, uw.unit, uw.user from UserWorkspace uw where uw.user.login like :login")
                .setParameter("login", data.getUsername())
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
    public ServerStatusResponse getStatus() {
        if (phase == null) {
            return getStatusResponse(ClientModeInitPhase.NOT_RUNNING);
        }

        if (phase.equals(ClientModeInitPhase.IMPORTING_FILE)) {
            String msg = messages.get("init.offinit.phase." + importer.getPhase().name());
            return new ServerStatusResponse(importer.getPhase().name(), messages.format(msg, importer.getImportingTable()));
        }

        return getStatusResponse(phase);
    }

    /**
     * Builds ServerStatusResponse to response status service.
     * @param phase
     * @return
     */
    private ServerStatusResponse getStatusResponse(Enum phase) {
        return new ServerStatusResponse(phase.name(), messages.get("init.offinit.phase." + phase.name()));
    }

    /**
     * Enum used to track the initialization progress
     */
    public enum ClientModeInitPhase {
        STARTING,
        DOWNLOADING_FILE,
        IMPORTING_FILE,
        NOT_RUNNING
    }
}
