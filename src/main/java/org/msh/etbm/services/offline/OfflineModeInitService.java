package org.msh.etbm.services.offline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.sync.SynchronizationException;
import org.msh.etbm.services.offline.ParentServerRequestService;
import org.msh.etbm.services.offline.ServerCredentialsData;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.msh.etbm.web.api.authentication.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Service to initialize an off-line mode instance.
 * Created by Mauricio on 21/11/2016.
 */
@Service
public class OfflineModeInitService {

    @Autowired
    ParentServerRequestService request;

    @Autowired
    DozerBeanMapper mapper;

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
        File file = request.downloadFile(data.getParentServerUrl(),
                "/api/sync/inifile/",
                loginRes.getAuthToken().toString(),
                "C:\\Users\\Mauricio\\Desktop"); //TODO: verificar o caminho correto

        // import file
        try {
            importer.importFile(file, true);
        } catch (IOException e) {
            throw new SynchronizationException("Importing process failed.");
        }

        // TODO: update systemconfig setting version, serverURL, and client flag

        System.out.println("hey");
    }

    private TypeFactory getTypeFactory() {
        return new ObjectMapper().getTypeFactory();
    }
}
