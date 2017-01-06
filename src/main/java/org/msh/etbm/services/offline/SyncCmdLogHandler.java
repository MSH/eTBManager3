package org.msh.etbm.services.offline;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.springframework.stereotype.Service;

/**
 * Service responsible for the command registration log of synchronization services
 *
 * Created by msantos on 19/12/16.
 */
@Service
public class SyncCmdLogHandler implements CommandLogHandler<Object, SynchronizationResponse> {

    @Override
    public void prepareLog(CommandHistoryInput in, Object request, SynchronizationResponse response) {
        in.setAction(CommandAction.EXEC);
        in.setUnitId(response.getUnitId());
        in.setUserId(response.getUserId());
        in.setWorkspaceId(response.getWorkspaceId());
    }

}
