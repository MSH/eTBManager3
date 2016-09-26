package org.msh.etbm.services.cases.treatment.edit;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Request to include a new medicine in the treatment regimen
 *
 * Created by rmemoria on 15/9/16.
 */
public class AddMedicineRequest {

    @NotNull
    private UUID caseId;

    @NotNull
    private UUID productId;

    @NotNull
    private Date iniDate;

    @NotNull
    private Date endDate;

    @NotNull
    private Integer doseUnit;

    @NotNull
    private Integer frequency;

    private String comments;

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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

    public Integer getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(Integer doseUnit) {
        this.doseUnit = doseUnit;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
