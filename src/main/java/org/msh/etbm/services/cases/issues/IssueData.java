package org.msh.etbm.services.cases.issues;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.services.cases.issues.followup.IssueFollowUpData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mauricio on 03/08/2016.
 */
public class IssueData {
    private UUID id;

    private boolean closed;

    private SynchronizableItem user;

    private Date creationDate;

    private Date lastAnswerDate;

    private String title;

    private String description;

    private List<IssueFollowUpData> followups = new ArrayList<IssueFollowUpData>();

    private SynchronizableItem unit;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public SynchronizableItem getUser() {
        return user;
    }

    public void setUser(SynchronizableItem user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastAnswerDate() {
        return lastAnswerDate;
    }

    public void setLastAnswerDate(Date lastAnswerDate) {
        this.lastAnswerDate = lastAnswerDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IssueFollowUpData> getFollowups() {
        return followups;
    }

    public void setFollowups(List<IssueFollowUpData> followups) {
        this.followups = followups;
    }

    public SynchronizableItem getUnit() {
        return unit;
    }

    public void setUnit(SynchronizableItem unit) {
        this.unit = unit;
    }
}
