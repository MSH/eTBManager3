package org.msh.etbm.services.sync.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.services.sync.SynchronizationException;
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

    @Autowired
    EntityManager entityManager;

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

        List<WorkspaceInfo> response = request.post(serverAddress,
                "/api/auth/workspaces",
                null,
                data,
                getTypeFactory().constructCollectionType(List.class, WorkspaceInfo.class),
                null);

        return response;
    }

    /**
     * Download and import init file.
     * @param data
     */
    public void initialize(ServerCredentialsData data) {
        if (isInitialized()) {
            throw new EntityValidationException(data, null, null, "init.offinit.error3");
        }

        String serverAddress = checkServerAddress(data.getParentServerUrl());

        // Login into remote server
        LoginResponse loginRes = request.post(serverAddress,
                "/api/auth/login",
                null,
                data,
                null,
                LoginResponse.class);

        // Download file
        File file = request.downloadFile(data.getParentServerUrl(),
                "/api/sync/inifile/",
                loginRes.getAuthToken().toString());

        // import file
        importer.importFile(file, true, serverAddress);

        file.delete();
    }

    private TypeFactory getTypeFactory() {
        return new ObjectMapper().getTypeFactory();
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
}
