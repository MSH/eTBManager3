package org.msh.etbm.services.sync.client.data;

/**
 * Created by Mauricio on 15/12/2016.
 */
public class RecordChangeEvent {

    private String table;
    private Object id;
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
