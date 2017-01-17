package org.msh.etbm.services.offline.server;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandStoreService;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer component to be called to generates the init file
 * Created by Mauricio on 12/01/2017.
 */
@Service
public class ServerInitService {

    @Autowired
    ServerFileGenerator serverFileGenerator;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    CommandStoreService commandStoreService;

    public File generateInitFile() throws SynchronizationException {
        // get parameters
        UUID unitId = userRequestService.getUserSession().getUnitId();
        UUID workspaceId = userRequestService.getUserSession().getWorkspaceId();
        UUID userId = userRequestService.getUserSession().getUserId();

        // generate file
        File file = serverFileGenerator.generate(unitId, workspaceId, Optional.empty());

        // register commandlog
        CommandHistoryInput in = new CommandHistoryInput();
        in.setWorkspaceId(workspaceId);
        in.setUnitId(unitId);
        in.setUserId(userId);
        in.setAction(CommandAction.EXEC);
        in.setType(CommandTypes.OFFLINE_SERVERINIT);

        commandStoreService.store(in);

        return file;
    }

}
