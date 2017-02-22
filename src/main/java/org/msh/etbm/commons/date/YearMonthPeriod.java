package org.msh.etbm.commons.date;

/**
 * Store information about a period of month/year
 *
 * Created by rmemoria on 2/12/16.
 */
public class YearMonthPeriod {

    private Integer iniYear;
    private Integer iniMonth;
    private Integer endYear;
    private Integer endMonth;

    public Integer getIniYear() {
        return iniYear;
    }

    public void setIniYear(Integer iniYear) {
        this.iniYear = iniYear;
    }

    public Integer getIniMonth() {
        return iniMonth;
    }

    public void setIniMonth(Integer iniMonth) {
        this.iniMonth = iniMonth;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
    }
}
