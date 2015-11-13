package org.msh.etbm.services.admin.substances;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.Item;
import org.msh.etbm.db.enums.MedicineLine;

import java.util.UUID;

/**
 * Information about a substance returned by the SubstanceService component
 *
 * Created by rmemoria on 12/11/15.
 */
public class SubstanceData extends Item<UUID> {

    private String shortName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customId;

    private MedicineLine line;

    private boolean prevTreatmentForm;

    private boolean dstResultForm;

    private Integer displayOrder;

    private boolean active;


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public MedicineLine getLine() {
        return line;
    }

    public void setLine(MedicineLine line) {
        this.line = line;
    }

    public boolean isPrevTreatmentForm() {
        return prevTreatmentForm;
    }

    public void setPrevTreatmentForm(boolean prevTreatmentForm) {
        this.prevTreatmentForm = prevTreatmentForm;
    }

    public boolean isDstResultForm() {
        return dstResultForm;
    }

    public void setDstResultForm(boolean dstResultForm) {
        this.dstResultForm = dstResultForm;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
