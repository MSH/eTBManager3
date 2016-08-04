package org.msh.etbm.services.cases.issues.followup;

import org.msh.etbm.commons.SynchronizableItem;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 03/08/2016.
 */
public class IssueFollowUpData {

    private UUID id;

    private String text;

    private SynchronizableItem user;

    private Date followupDate;

    private SynchronizableItem unit;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SynchronizableItem getUser() {
        return user;
    }

    public void setUser(SynchronizableItem user) {
        this.user = user;
    }

    public Date getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }

    public SynchronizableItem getUnit() {
        return unit;
    }

    public void setUnit(SynchronizableItem unit) {
        this.unit = unit;
    }
}
