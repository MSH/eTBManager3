package org.msh.etbm.services.offline.server;

import org.msh.etbm.services.offline.query.TableChangesTraverser;

import java.io.IOException;
import java.util.UUID;

/**
 * Used by {@link TableChangesTraverser} to traverse each deleted record
 *
 * Created by rmemoria on 8/11/16.
 */
public interface DeletedRecordTraverseListener {

    /**
     * Called on each deleted record
     * @param id the ID of the deleted record
     */
    void onDeletedRecord(UUID id) throws IOException;
}
