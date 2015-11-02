package org.msh.etbm.db.entities;

import org.msh.etbm.services.admin.units.UnitType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Store information about a laboratory. Laboratory plays roles in exams and inventory management
 */
@Entity
@DiscriminatorValue("lab")
public class Laboratory extends Unit {

    /**
     * True if laboratory perform the following tests
     */
    private boolean performCulture;
    private boolean performMicroscopy;
    private boolean performDst;
    private boolean performXpert;

    public boolean isPerformCulture() {
        return performCulture;
    }

    public void setPerformCulture(boolean performCulture) {
        this.performCulture = performCulture;
    }

    public boolean isPerformMicroscopy() {
        return performMicroscopy;
    }

    public void setPerformMicroscopy(boolean performMicroscopy) {
        this.performMicroscopy = performMicroscopy;
    }

    public boolean isPerformDst() {
        return performDst;
    }

    public void setPerformDst(boolean performDst) {
        this.performDst = performDst;
    }

    public boolean isPerformXpert() {
        return performXpert;
    }

    public void setPerformXpert(boolean performXpert) {
        this.performXpert = performXpert;
    }

    @Override
    public UnitType getType() {
        return UnitType.LAB;
    }

}
