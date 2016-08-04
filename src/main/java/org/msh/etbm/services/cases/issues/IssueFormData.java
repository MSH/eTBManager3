package org.msh.etbm.services.cases.issues;

import org.msh.etbm.services.cases.CaseEntityFormData;

import java.util.Optional;

/**
 * Created by Mauricio on 03/08/2016.
 */
public class IssueFormData extends CaseEntityFormData {
    private Optional<String> title;

    private Optional<String> description;

    private Optional<Boolean> closed;

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public Optional<Boolean> getClosed() {
        return closed;
    }

    public void setClosed(Optional<Boolean> closed) {
        this.closed = closed;
    }
}
