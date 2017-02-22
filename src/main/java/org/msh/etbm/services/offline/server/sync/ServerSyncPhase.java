package org.msh.etbm.services.offline.server.sync;

/**
 * Phases of server sync process
 * Created by Mauricio on 07/01/2017.
 */
public enum ServerSyncPhase {
    IMPORTING_CLIENT_FILE,
    GENERATING_SERVER_FILE,
    WAITING_SERVER_FILE_DOWNLOAD,
    WAITING_SERVER_FILE_IMPORTING
}
