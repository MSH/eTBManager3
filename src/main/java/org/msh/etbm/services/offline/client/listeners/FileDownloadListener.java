package org.msh.etbm.services.offline.client.listeners;

import org.msh.etbm.services.offline.client.ClientInitService;

import java.io.File;

/**
 * Used by {@link ClientInitService} to call importing after asynchronously download the file
 * Created by Mauricio on 13/12/2016.
 */
public interface FileDownloadListener {

    void afterDownload(File downloadedFile, boolean success);

}
