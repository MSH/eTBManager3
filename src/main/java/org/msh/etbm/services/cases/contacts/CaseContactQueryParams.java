package org.msh.etbm.services.cases.contacts;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.UUID;

/**
 * Created by rmemoria on 6/1/16.
 */
public class CaseContactQueryParams extends EntityQueryParams {

    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_ITEM = "item";

    private UUID tbcaseId;

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }
}
