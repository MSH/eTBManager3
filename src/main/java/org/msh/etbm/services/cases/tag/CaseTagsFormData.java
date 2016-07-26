package org.msh.etbm.services.cases.tag;

import org.msh.etbm.db.entities.Tag;

import java.util.List;
import java.util.UUID;

/**
 * Bean to tranfer update information of manual tags
 * Created by Mauricio on 25/07/2016.
 */
public class CaseTagsFormData {
    private List<UUID> tagIds;
    private List<String> newTags;
    private UUID tbcaseId;

    public List<UUID> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<UUID> tagIds) {
        this.tagIds = tagIds;
    }

    public List<String> getNewTags() {
        return newTags;
    }

    public void setNewTags(List<String> newTags) {
        this.newTags = newTags;
    }

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }
}
