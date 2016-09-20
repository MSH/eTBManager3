package org.msh.etbm.services.cases.casemove;

import org.msh.etbm.services.cases.CaseActionResponse;

import java.util.UUID;

/**
 * Created by Mauricio on 14/09/2016.
 */
public class CaseMoveResponse extends CaseActionResponse {
    UUID unitToId;

    CaseMoveResponse(UUID tbcaseId, String tbcaseDisplayString, UUID unitToId) {
        super(tbcaseId, tbcaseDisplayString);
        this.unitToId = unitToId;
    }

    public UUID getUnitToId() {
        return unitToId;
    }

    public void setUnitToId(UUID unitToId) {
        this.unitToId = unitToId;
    }
}
