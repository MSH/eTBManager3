package org.msh.etbm.services.cases.issues.followup;

import java.util.UUID;

/**
 * Created by Mauricio on 03/08/2016.
 */
public class IssueFollowUpFormData {

    private UUID issueId;

    private String text;

    public UUID getIssueId() {
        return issueId;
    }

    public void setIssueId(UUID issueId) {
        this.issueId = issueId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}