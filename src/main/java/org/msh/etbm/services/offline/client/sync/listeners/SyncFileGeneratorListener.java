package org.msh.etbm.services.offline.client.sync.listeners;

import org.msh.etbm.services.offline.client.sync.ClientSyncService;

import java.io.File;

/**
 * Used by {@link ClientSyncService} to call file uploading after generating the file
 * Created by Mauricio on 13/12/2016.
 */
public interface SyncFileGeneratorListener {

    void afterGenerate(File generatedFile);

}
