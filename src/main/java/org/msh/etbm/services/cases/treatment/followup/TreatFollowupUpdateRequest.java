package org.msh.etbm.services.cases.treatment.followup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Request sent to the {@link TreatmentFollowupService} in order to update the treatment followup
 * of a case for a given month of the treatment
 *
 * Created by rmemoria on 24/8/16.
 */
public class TreatFollowupUpdateRequest {
    @NotNull
    private UUID caseId;

    @NotNull
    @Min(1950)
    @Max(2050)
    private int year;

    @NotNull
    @Min(0)
    @Max(11)
    private int month;

    @NotNull
    private List<TreatDayUpdateRequest> days;


    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<TreatDayUpdateRequest> getDays() {
        return days;
    }

    public void setDays(List<TreatDayUpdateRequest> days) {
        this.days = days;
    }
}
