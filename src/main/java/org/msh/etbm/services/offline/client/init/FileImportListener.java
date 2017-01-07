package org.msh.etbm.services.offline.client.init;

import java.io.File;

/**
 * Used by {@link ClientModeInitService} to call initialization process finishing after asynchronously importing the file
 * Created by Mauricio on 13/12/2016.
 */
public interface FileImportListener {

    void afterImport(File importedFile, Integer fileVersion);

}
