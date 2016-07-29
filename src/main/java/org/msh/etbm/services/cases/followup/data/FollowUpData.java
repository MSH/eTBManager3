package org.msh.etbm.services.cases.followup.data;

import org.msh.etbm.services.cases.CaseEventData;

/**
 * Follow up information to be sent to the client
 * Created by msantos on 7/7/16.
 */
public class FollowUpData {
    /**
     * The type of follow up
     */
    private String type;

    /**
     * The follow up name message key
     */
    private String name;

    /**
     * The month of treatment when the follow up was collected
     */
    private String monthOfTreatment;

    /**
     * The follow up specific data
     */
    private CaseEventData data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonthOfTreatment() {
        return monthOfTreatment;
    }

    public void setMonthOfTreatment(String monthOfTreatment) {
        this.monthOfTreatment = monthOfTreatment;
    }

    public CaseEventData getData() {
        return data;
    }

    public void setData(CaseEventData data) {
        this.data = data;
    }
}
