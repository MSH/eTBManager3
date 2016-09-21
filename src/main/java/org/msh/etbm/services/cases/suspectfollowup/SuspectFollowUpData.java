package org.msh.etbm.services.cases.suspectfollowup;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Mauricio on 17/09/2016.
 */
public class SuspectFollowUpData {
    Map<String, Object> doc;

    UUID tbcaseId;

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }

    public Map<String, Object> getDoc() {
        return doc;
    }

    public void setDoc(Map<String, Object> doc) {
        this.doc = doc;
    }
}
