package org.msh.etbm.services.cases.followup.examxray;

import org.msh.etbm.services.cases.CaseEventData;

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
