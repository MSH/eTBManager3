package org.msh.etbm.services.offline.server;

import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.SynchronizationResponse;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Mauricio on 12/01/2017.
 */
public class ServerInitService {

    @Autowired
    ServerFileGenerator serverFileGenerator;

    @Autowired
    UserRequestService userRequestService;

    public SynchronizationResponse generateInitFile() throws SynchronizationException {
        // get parameters
        UUID unitId = userRequestService.getUserSession().getUnitId();
        UUID workspaceId = userRequestService.getUserSession().getWorkspaceId();
        UUID userId = userRequestService.getUserSession().getUserId();

        // generate file
        SynchronizationResponse resp = serverFileGenerator.generate(unitId, workspaceId, userId, Optional.empty());

        // todo register command log

        return resp;
    }

}
