package org.msh.etbm.services.cases;

import java.util.Date;
import java.util.UUID;

/**
 * Created by msantos on 13/7/16.
 */
public abstract class CaseEventData {
    private UUID id;
    private Date date;
    private String comments;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
