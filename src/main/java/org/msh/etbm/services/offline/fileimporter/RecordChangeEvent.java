package org.msh.etbm.services.offline.fileimporter;

/**
 * Event type that is triggered when a record is created, updated or deleted.
 * Created by Mauricio on 15/12/2016.
 */
public class RecordChangeEvent {

    /**
     * The table name
     */
    private String table;
    /**
     * The record id
     */
    private Object id;
    /**
     * The action: CREATE, UPDATE or DELETE
     */
    private String action;

    public RecordChangeEvent(String table, Object id, String action) {
        this.table = table;
        this.id = id;
        this.action = action;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
