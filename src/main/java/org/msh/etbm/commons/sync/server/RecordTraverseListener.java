package org.msh.etbm.commons.sync.server;

import java.io.IOException;
import java.util.Map;

/**
 * Interface used by {@link TableChangesTraverser} to inform about new and updated records
 *
 * Created by rmemoria on 8/11/16.
 */
public interface RecordTraverseListener {

    /**
     * Called for each record found
     * @param rec the Map containing the fields and values of the record
     * @param index the zero-based index of the record
     */
    void onRecord(Map<String, Object> rec, int index) throws IOException;
}
