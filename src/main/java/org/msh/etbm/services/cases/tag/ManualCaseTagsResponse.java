package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.Item;
import org.msh.etbm.services.cases.CaseActionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mauricio on 26/07/2016.
 */
public class ManualCaseTagsResponse extends CaseActionResponse {

    List<Item<UUID>> prevManualTags;
    List<Item<UUID>> newManualTags;

    public ManualCaseTagsResponse() {
        super();
        this.prevManualTags = new ArrayList<>();
        this.newManualTags = new ArrayList<>();
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
