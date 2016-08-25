package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mauricio on 26/07/2016.
 */
public class ManualCaseTagsResponse {
    UUID tbcaseId;
    String tbcaseDisplayString;
    List<Item<UUID>> prevManualTags;
    List<Item<UUID>> newManualTags;

    public ManualCaseTagsResponse() {
        this.prevManualTags = new ArrayList<>();
        this.newManualTags = new ArrayList<>();
    }

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }

    public String getTbcaseDisplayString() {
        return tbcaseDisplayString;
    }

    public void setTbcaseDisplayString(String tbcaseDisplayString) {
        this.tbcaseDisplayString = tbcaseDisplayString;
    }

    public List<Item<UUID>> getPrevManualTags() {
        return prevManualTags;
    }

    public void setPrevManualTags(List<Item<UUID>> prevManualTags) {
        this.prevManualTags = prevManualTags;
    }

    public List<Item<UUID>> getNewManualTags() {
        return newManualTags;
    }

    public void setNewManualTags(List<Item<UUID>> newManualTags) {
        this.newManualTags = newManualTags;
    }
}
