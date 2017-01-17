package org.msh.etbm.services.offline.fileimporter;

import org.msh.etbm.services.offline.client.ClientInitService;

import java.io.File;

/**
 * Used by {@link ClientInitService} to call initialization process finishing after asynchronously importing the file
 * Created by Mauricio on 13/12/2016.
 */
public interface FileImportListener {

    void afterImport(File importedFile, ImportResponse response);

}
