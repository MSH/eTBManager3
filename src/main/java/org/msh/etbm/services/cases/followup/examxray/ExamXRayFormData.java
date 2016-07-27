package org.msh.etbm.services.cases.followup.examxray;

import org.msh.etbm.services.cases.CaseEventFormData;

import java.util.Optional;

/**
 * Created by msantos on 14/7/16.
 */
public class ExamXRayFormData extends CaseEventFormData {

    private Optional<String> evolution;
    private Optional<String> presentation;

    public Optional<String> getEvolution() {
        return evolution;
    }

    public void setEvolution(Optional<String> evolution) {
        this.evolution = evolution;
    }

    public Optional<String> getPresentation() {
        return presentation;
    }

    public void setPresentation(Optional<String> presentation) {
        this.presentation = presentation;
    }
}
