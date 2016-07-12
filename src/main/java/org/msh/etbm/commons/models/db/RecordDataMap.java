package org.msh.etbm.commons.models.db;

import java.util.HashMap;
import java.util.UUID;

/**
 * Store information about a record. This is a simple representation of the
 * Created by rmemoria on 12/7/16.
 */
public class RecordDataMap extends HashMap<String, Object> {

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String s = id != null ? "id=" + id.toString() + " " : "NEW ";
        return s + super.toString();
    }
}
