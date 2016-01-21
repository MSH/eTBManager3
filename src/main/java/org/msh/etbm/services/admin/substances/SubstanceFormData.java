package org.msh.etbm.services.admin.substances;

import org.msh.etbm.db.enums.MedicineLine;

import java.util.Optional;

/**
 * Substance request data used in CRUD operations to create a new one or update the data of an existing one
 *
 * Created by rmemoria on 12/11/15.
 */
public class SubstanceFormData {

    /**
     * Available profiles
     */
    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";

    /**
     * Available sorting options
     */
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_DISPLAYORDER = "displayOrder";

    private Optional<String> name;
    private Optional<String> shortName;
    private Optional<MedicineLine> line;
    private Optional<Boolean> prevTreatmentForm;
    private Optional<Boolean> dstResultForm;
    private Optional<Integer> displayOrder;
    private Optional<String> customId;


    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getShortName() {
        return shortName;
    }

    public void setShortName(Optional<String> shortName) {
        this.shortName = shortName;
    }

    public Optional<MedicineLine> getLine() {
        return line;
    }

    public void setLine(Optional<MedicineLine> line) {
        this.line = line;
    }

    public Optional<Boolean> getPrevTreatmentForm() {
        return prevTreatmentForm;
    }

    public void setPrevTreatmentForm(Optional<Boolean> prevTreatmentForm) {
        this.prevTreatmentForm = prevTreatmentForm;
    }

    public Optional<Boolean> getDstResultForm() {
        return dstResultForm;
    }

    public void setDstResultForm(Optional<Boolean> dstResultForm) {
        this.dstResultForm = dstResultForm;
    }

    public Optional<Integer> getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Optional<Integer> displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }
}
