package org.msh.etbm.services.admin.regimens;

import org.msh.etbm.db.enums.CaseClassification;

/**
 * Regimen information to be returned from the service to the client
 *
 * Created by rmemoria on 6/1/16.
 */
public class RegimenData {
    private String name;
    private CaseClassification caseClassification;
    private String customId;
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CaseClassification getCaseClassification() {
        return caseClassification;
    }

    public void setCaseClassification(CaseClassification caseClassification) {
        this.caseClassification = caseClassification;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
