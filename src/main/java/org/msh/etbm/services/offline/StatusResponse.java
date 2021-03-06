package org.msh.etbm.services.offline;

/**
 * Data class to response initialization progress
 * Created by Mauricio on 13/12/2016.
 */
public class StatusResponse {
    /**
     * The status id
     */
    private String id;

    /**
     * The tatus message
     */
    private String title;

    /**
     * The sync token
     */
    private String token;

    public StatusResponse() {
        super();
    }

    public StatusResponse(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public StatusResponse(String id, String title, String token) {
        this.id = id;
        this.title = title;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
