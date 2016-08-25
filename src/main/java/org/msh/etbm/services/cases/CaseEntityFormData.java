package org.msh.etbm.services.cases;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by msantos on 13/7/16.
 */
public abstract class CaseEntityFormData {
    private Optional<UUID> tbcaseId;

    public Optional<UUID> getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(Optional<UUID> tbcaseId) {
        this.tbcaseId = tbcaseId;
    }
}
