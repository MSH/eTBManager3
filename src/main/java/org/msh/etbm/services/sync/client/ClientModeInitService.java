package org.msh.etbm.services.sync.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.web.api.authentication.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Returns all workspaces attached to the user that matches with credentials on param.
     * @param data
     * @return
     */
    public List<WorkspaceInfo> findWorkspaces(ServerCredentialsData data) {
        List<WorkspaceInfo> response = request.post(data.getParentServerUrl(),
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
        // Login into remote server
        LoginResponse loginRes = request.post(data.getParentServerUrl(),
                "/api/auth/login",
                null,
                data,
                null,
                LoginResponse.class);

        // Download file
        /*File file = request.downloadFile(data.getParentServerUrl(),
                "/api/sync/inifile/",
                loginRes.getAuthToken().toString());*/

        File file = new File("C:\\Users\\Mauricio\\Desktop\\MSH_Demo.etbm");

        // import file
        importer.importFile(file, true);

        // TODO: [MSANTOS] DELETE THE FILE HERE
        // TODO: [MSANTOS] update systemconfig setting version, serverURL, and client flag
    }

    private TypeFactory getTypeFactory() {
        return new ObjectMapper().getTypeFactory();
    }
}