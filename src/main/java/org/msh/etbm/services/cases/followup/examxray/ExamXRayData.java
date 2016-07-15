package org.msh.etbm.services.cases.followup.examxray;

import org.msh.etbm.db.enums.HIVResult;
import org.msh.etbm.services.cases.followup.data.CaseEventData;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by msantos on 14/7/16.
 */
public class ExamXRayData extends CaseEventData {

    private String evolution;
    private String presentation;

    public String getEvolution() {
        return evolution;
    }

    public void setEvolution(String evolution) {
        this.evolution = evolution;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
}
