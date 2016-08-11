package org.msh.etbm.services.cases.treatment.data;

import org.msh.etbm.db.enums.TreatmentDayStatus;

/**
 * Store the status of the treatment monitoring for an specific day of the treatment. This class
 * is contained by instances of {@link MonthlyFollowup} objects
 *
 * Created by rmemoria on 10/8/16.
 */
public class FollowupDay {
    /**
     * The day of the month
     */
    private int day;

    /**
     * The status of the treatment monitoring for that day
     */
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
