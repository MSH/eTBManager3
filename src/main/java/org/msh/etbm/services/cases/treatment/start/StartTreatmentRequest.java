package org.msh.etbm.services.cases.treatment.start;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Request containing data to start an individualized regimen treatment for a case
 *
 * Created by rmemoria on 31/8/16.
 */
public class StartTreatmentRequest {

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
     * The regimen ID, required if it is a standardized regimen (so the prescriptions must be null)
     */
    private UUID regimenId;

    /**
     * The initial date of the treatment
     */
    @NotNull
    private Date iniDate;

    /**
     * List of prescribed medicines, required if the regimen is not informed (so it is an individualized regimen)
     */
    @Valid
    private List<PrescriptionRequest> prescriptions;


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

    public List<PrescriptionRequest> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionRequest> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
