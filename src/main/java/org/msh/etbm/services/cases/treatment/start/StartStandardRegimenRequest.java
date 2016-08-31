package org.msh.etbm.services.cases.treatment.start;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Request data to start a standard regimen treatment
 *
 * Created by rmemoria on 31/8/16.
 */
public class StartStandardRegimenRequest {

    /**
     * The ID of the unit to start the treatment. If null, the owner unit of the case
     * will be used instead
     */
    private UUID unitId;

    /**
     * The ID of the case
     */
    @NotNull
    private UUID caseId;

    /**
     * The regimen ID
     */
    @NotNull
    private UUID regimenId;

    /**
     * The initial date of the treatment
     */
    @NotNull
    private Date iniDate;


    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public UUID getRegimenId() {
        return regimenId;
    }

    public void setRegimenId(UUID regimenId) {
        this.regimenId = regimenId;
    }

    public Date getIniDate() {
        return iniDate;
    }

    public void setIniDate(Date iniDate) {
        this.iniDate = iniDate;
    }
}
