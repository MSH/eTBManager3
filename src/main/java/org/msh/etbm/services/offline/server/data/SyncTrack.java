package org.msh.etbm.services.offline.server.data;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.services.offline.server.ServerSyncService;
import org.msh.etbm.services.offline.server.sync.ServerSyncPhase;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 07/01/2017.
 */
public class SyncTrack {
    Date iniDateTime;
    String syncToken;
    File clientSyncFile;
    File serverSyncFile;
    UUID unitId;
    ServerSyncPhase phase;

    public SyncTrack(String syncToken, File clientSyncFile) {
        iniDateTime = DateUtils.getDate();
        this.syncToken = syncToken;
        this.clientSyncFile = clientSyncFile;
    }

    public Date getIniDateTime() {
        return iniDateTime;
    }

    public String getSyncToken() {
        return syncToken;
    }

    public void setSyncToken(String syncToken) {
        this.syncToken = syncToken;
    }

    public File getClientSyncFile() {
        return clientSyncFile;
    }

    public void setClientSyncFile(File clientSyncFile) {
        this.clientSyncFile = clientSyncFile;
    }

    public File getServerSyncFile() {
        return serverSyncFile;
    }

    public void setServerSyncFile(File serverSyncFile) {
        this.serverSyncFile = serverSyncFile;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public ServerSyncPhase getPhase() {
        return phase;
    }

    public void setPhase(ServerSyncPhase phase) {
        this.phase = phase;
    }
}
