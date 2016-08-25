package org.msh.etbm.services.cases.treatment.followup;

import org.msh.etbm.db.enums.TreatmentDayStatus;

import javax.validation.constraints.NotNull;

/**
 * Created by rmemoria on 24/8/16.
 */
public class TreatDayUpdateRequest {

    @NotNull
    private int day;
    @NotNull
    private TreatmentDayStatus status;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public TreatmentDayStatus getStatus() {
        return status;
    }

    public void setStatus(TreatmentDayStatus status) {
        this.status = status;
    }
}
