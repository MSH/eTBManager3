package org.msh.etbm.services.cases.followup.data;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by msantos on 13/7/16.
 */
public abstract class CaseEventFormData {
    private Optional<Date> date;
    private Optional<String> comments;
    private Optional<UUID> tbcaseId;

    public Optional<Date> getDate() {
        return date;
    }

    public void setDate(Optional<Date> date) {
        this.date = date;
    }

    public Optional<String> getComments() {
        return comments;
    }

    public void setComments(Optional<String> comments) {
        this.comments = comments;
    }

    public Optional<UUID> getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(Optional<UUID> tbcaseId) {
        this.tbcaseId = tbcaseId;
    }
}
