package org.msh.etbm.services.cases.treatment.edit;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Request to update a given prescription of the patient treatment
 *
 * Created by rmemoria on 15/9/16.
 */
public class PrescriptionUpdateRequest {

    /**
     * The prescription ID in the table prescribedMedicines
     */
    @NotNull
    private UUID prescriptionId;

    /**
     * The initial date of the prescription. Optional
     */
    private Date iniDate;

    /**
     * The final date of the prescription. Optional
     */
    private Date endDate;

    /**
     * The weekly frequency
     */
    @Min(1)
    @Max(7)
    private Integer frequency;

    /**
     * The dose unit prescribed to the patient
     */
    @Min(1)
    @Max(10)
    private Integer doseUnit;

    /**
     * Any comment given by the user
     */
    private String comment;

    /**
     * If the period is changed to a narrow period, and this flag is true, the remaining period outside
     * the new period will be preserved
     */
    private boolean preservePrevPeriod;


    public UUID getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(UUID prescriptionId) {
        this.prescriptionId = prescriptionId;
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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(Integer doseUnit) {
        this.doseUnit = doseUnit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPreservePrevPeriod() {
        return preservePrevPeriod;
    }

    public void setPreservePrevPeriod(boolean preservePrevPeriod) {
        this.preservePrevPeriod = preservePrevPeriod;
    }
}
