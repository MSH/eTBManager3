package org.msh.etbm.services.offline.server.sync;

/**
 * Created by Mauricio on 07/01/2017.
 */
public enum ServerSyncPhase {
    IMPORTING_CLIENT_FILE,
    GENERATING_SERVER_FILE,
    WAITING_SERVER_FILE_DOWNLOAD,
    WAITING_SERVER_FILE_IMPORTING;

    public String getMessageKey() {
        return "sync.server.phase." + this.name();
    }
}
