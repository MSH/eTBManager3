package org.msh.etbm.services.offline.client.data;

/**
 * Data class to response initialization progress
 * Created by Mauricio on 13/12/2016.
 */
public class ServerStatusResponse {
    private String id;
    private String title;

    public ServerStatusResponse(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
