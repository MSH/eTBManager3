package org.msh.etbm.services.init;

import org.msh.etbm.commons.commands.CommandLog;

import java.util.UUID;

/**
 * Register a workspace in the system during its initialization process
 *
 * Created by rmemoria on 10/10/15.
 */
public interface RegisterWorkspaceService {
    UUID run(RegisterWorkspaceRequest req);
}
