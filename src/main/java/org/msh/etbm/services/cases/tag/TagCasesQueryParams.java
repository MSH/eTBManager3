package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.UUID;

/**
 * Created by Mauricio on 09/08/2016.
 */
public class TagCasesQueryParams extends EntityQueryParams {
    UUID tagId;
    UUID unitId;

    public UUID getTagId() {
        return tagId;
    }

    public void setTagId(UUID tagId) {
        this.tagId = tagId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }
}
