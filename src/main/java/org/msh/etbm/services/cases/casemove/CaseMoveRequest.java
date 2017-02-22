package org.msh.etbm.services.cases.casemove;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 12/09/2016.
 */
public class CaseMoveRequest {

    private UUID tbcaseId;
    private Date moveDate;
    private UUID unitToId;

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }

    public Date getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(Date moveDate) {
        this.moveDate = moveDate;
    }

    public UUID getUnitToId() {
        return unitToId;
    }

    public void setUnitToId(UUID unitToId) {
        this.unitToId = unitToId;
    }

}
