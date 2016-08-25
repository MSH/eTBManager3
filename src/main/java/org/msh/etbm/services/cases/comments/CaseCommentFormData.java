package org.msh.etbm.services.cases.comments;

import org.msh.etbm.db.enums.CaseDataGroup;
import org.msh.etbm.services.cases.CaseEntityFormData;

import java.util.Optional;

/**
 * Created by Mauricio on 27/07/2016.
 */
public class CaseCommentFormData extends CaseEntityFormData {

    private Optional<String> comment;

    private Optional<CaseDataGroup> group;

    public Optional<String> getComment() {
        return comment;
    }

    public void setComment(Optional<String> comment) {
        this.comment = comment;
    }

    public Optional<CaseDataGroup> getGroup() {
        return group;
    }

    public void setGroup(Optional<CaseDataGroup> group) {
        this.group = group;
    }
}
