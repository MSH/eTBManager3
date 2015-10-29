package org.msh.etbm.services.admin.units;

/**
 * Request data about a laboratory
 *
 * Created by rmemoria on 28/10/15.
 */
public class LaboratoryRequest extends UnitRequest {

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
}
