package org.msh.etbm.services.cases.treatment.edit;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Request object to change the treatment as a whole, like treatment period, regimen and
 * treatment unit
 *
 * Created by rmemoria on 15/9/16.
 */
public class TreatmentUpdateRequest {

    @NotNull
    private UUID caseId;

    private Date iniDate;

    private Date endDate;

    private UUID regimenId;

    private UUID unitId;

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public Date getIniDate() {
        return iniDate;
    }

    public void setIniDate(Date iniDate) {
        this.iniDate = iniDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public UUID getRegimenId() {
        return regimenId;
    }

    public void setRegimenId(UUID regimenId) {
        this.regimenId = regimenId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }
}
