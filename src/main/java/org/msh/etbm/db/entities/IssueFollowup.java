package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Store information about a follow up of an issue. A follow up contains
 * an answer given by an user to a given issue.
 *
 * @author Ricardo Memoria
 */
@Entity
@Table(name = "issuefollowup")
public class IssueFollowup extends Synchronizable {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Lob
    @NotNull
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date followupDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    @NotNull
    private Unit unit;


    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the followupDate
     */
    public Date getFollowupDate() {
        return followupDate;
    }

    /**
     * @param followupDate the followupDate to set
     */
    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }

    /**
     * @return the issue
     */
    public Issue getIssue() {
        return issue;
    }

    /**
     * @param issue the issue to set
     */
    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
