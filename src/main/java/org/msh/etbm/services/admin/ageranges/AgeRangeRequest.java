package org.msh.etbm.services.admin.ageranges;

/**
 * Request containing information about the age range to update or create
 * Created by rmemoria on 6/1/16.
 */
public class AgeRangeRequest {

    private int iniAge;
    private int endAge;

    public int getIniAge() {
        return iniAge;
    }

    public void setIniAge(int iniAge) {
        this.iniAge = iniAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public void setEndAge(int endAge) {
        this.endAge = endAge;
    }
}
