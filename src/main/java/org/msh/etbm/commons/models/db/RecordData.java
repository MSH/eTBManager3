package org.msh.etbm.commons.models.db;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Store information about a record. This is a simple representation of the
 * Created by rmemoria on 12/7/16.
 */
public class RecordData {

    private UUID id;

    private Map<String, Object> values = new HashMap<>();

    public RecordData() {
        super();
    }

    public RecordData(UUID id, Map<String, Object> values) {
        this.id = id;
        this.values.putAll(values);
    }

    public RecordData(Map<String, Object> values) {
        this.values.putAll(values);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        String s = id != null ? "id=" + id.toString() + " " : "NEW ";
        return s + values.toString();
    }
}
