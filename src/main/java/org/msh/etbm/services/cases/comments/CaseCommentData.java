package org.msh.etbm.services.cases.comments;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.enums.CaseDataGroup;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 27/07/2016.
 */
public class CaseCommentData {

    private UUID id;

    private SynchronizableItem user;

    private Date date;

    private String comment;

    private CaseDataGroup group;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SynchronizableItem getUser() {
        return user;
    }

    public void setUser(SynchronizableItem user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CaseDataGroup getGroup() {
        return group;
    }

    public void setGroup(CaseDataGroup group) {
        this.group = group;
    }
}
