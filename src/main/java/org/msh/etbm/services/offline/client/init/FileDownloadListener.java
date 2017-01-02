package org.msh.etbm.services.offline.client.init;

import java.io.File;

/**
 * Used by {@link ClientModeInitService} to call importing after asynchronously download the file
 * Created by Mauricio on 13/12/2016.
 */
public interface FileDownloadListener {

    void afterDownload(File downloadedFile);

}
