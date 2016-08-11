package org.msh.etbm.services.cases.treatment.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;
import java.util.List;

/**
 * Store information about a treatment followup for an specific month of the treatment
 *
 * Created by rmemoria on 10/8/16.
 */
public class MonthlyFollowup {
    /**
     * Year of the treatment
     */
    private int year;

    /**
     * Month of the treatment
     */
    private int month;

    /**
     * List of days with treatment followup information
     */
    private List<FollowupDay> days;

    /**
     * If informed, the initial day the treatment. It must be informed in the first instance
     * of the list of followup data
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer iniDay;

    /**
     * If informed, the last day of the treatment. It must be informed in the last instance of
     * the list of followup data
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer endDay;



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

    public List<FollowupDay> getDays() {
        return days;
    }

    public void setDays(List<FollowupDay> days) {
        this.days = days;
    }

    public Integer getIniDay() {
        return iniDay;
    }

    public void setIniDay(Integer iniDay) {
        this.iniDay = iniDay;
    }

    public Integer getEndDay() {
        return endDay;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }
}
