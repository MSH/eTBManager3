package org.msh.etbm.services.offline;

import java.io.File;
import java.util.UUID;

/**
 * Created by Mauricio on 19/12/2016.
 */
public class SynchronizationResponse {

    private File file;
    private UUID unitId;
    private UUID workspaceId;
    private UUID userId;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

}
