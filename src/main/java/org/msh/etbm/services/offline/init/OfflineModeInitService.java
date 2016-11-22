package org.msh.etbm.services.offline.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.dozer.DozerBeanMapper;
import org.msh.etbm.services.offline.init.data.OfflineModeInitData;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Returns all workspaces attached to the user that matches with credentials on param.
     * @param data
     * @return
     */
    public List<WorkspaceInfo> findWorkspaces(OfflineModeInitData data) {
        List<WorkspaceInfo> response = request.post(data.getParentServerUrl(),
                "/api/auth/workspaces",
                null,
                data,
                getTypeFactory().constructCollectionType(List.class, WorkspaceInfo.class));

        return response;
    }

    /**
     * Download and import init file.
     * @param data
     */
    public void initialize(OfflineModeInitData data) {
        System.out.println("Downloading");
        System.out.println("Importing....");
    }

    private TypeFactory getTypeFactory() {
        return new ObjectMapper().getTypeFactory();
    }
}
