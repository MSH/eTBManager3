package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.enums.XRayBaseline;
import org.msh.etbm.db.enums.XRayEvolution;
import org.msh.etbm.db.enums.XRayResult;

import javax.persistence.*;

/**
 * Stores information about an X-Ray exam of a case
 *
 * @author Ricardo Lima
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "examxray")
public class ExamXRay extends CaseEvent {

    @Column(length = 50)
    private String evolution;

    @Column(length = 50)
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
