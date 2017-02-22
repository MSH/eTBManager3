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

    @NotNull
    private Date endDate;

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
