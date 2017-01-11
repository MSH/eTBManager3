package org.msh.etbm.services.offline.fileimporter;

import java.util.UUID;

/**
 * Created by Mauricio on 10/01/2017.
 */
public class ImportResponse {

    private Integer version;
    private UUID syncUnitId;

    public ImportResponse(Integer version, UUID syncUnitId) {
        this.version = version;
        this.syncUnitId = syncUnitId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getSyncUnitId() {
        return syncUnitId;
    }

    public void setSyncUnitId(UUID syncUnitId) {
        this.syncUnitId = syncUnitId;
    }
}
