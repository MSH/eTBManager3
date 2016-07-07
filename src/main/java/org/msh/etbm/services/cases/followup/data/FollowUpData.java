package org.msh.etbm.services.cases.followup.data;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.entities.CaseEvent;
import org.msh.etbm.services.cases.treatment.data.PrescriptionData;
import org.msh.etbm.services.cases.treatment.data.TreatmentUnitData;

import java.util.List;

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
    private String nameKey;

    /**
     * The month of treatment when the follow up was collected
     */
    private String monthOfTreatment;

    /**
     * The follow up specific data
     */
    private Object data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getMonthOfTreatment() {
        return monthOfTreatment;
    }

    public void setMonthOfTreatment(String monthOfTreatment) {
        this.monthOfTreatment = monthOfTreatment;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
