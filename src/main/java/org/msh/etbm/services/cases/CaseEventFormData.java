package org.msh.etbm.services.cases;

import java.util.Date;
import java.util.Optional;

/**
 * Created by msantos on 13/7/16.
 */
public abstract class CaseEventFormData extends CaseEntityFormData {
    private Optional<Date> date;
    private Optional<String> comments;

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

}
