package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Laboratory extends Unit {

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
