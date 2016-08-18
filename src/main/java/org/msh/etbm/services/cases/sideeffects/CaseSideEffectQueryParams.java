package org.msh.etbm.services.cases.sideeffects;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.UUID;

/**
 * Created by Mauricio on 12/08/2016.
 */
public class CaseSideEffectQueryParams extends EntityQueryParams {
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
