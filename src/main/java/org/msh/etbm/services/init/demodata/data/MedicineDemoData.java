package org.msh.etbm.services.init.demodata.data;

import org.msh.etbm.db.enums.MedicineCategory;
import org.msh.etbm.db.enums.MedicineLine;

/**
 * Stores data about medicines created as demonstration data
 * Created by Mauricio on 19/10/2016.
 */
public class MedicineDemoData {

    private String name;

    private String shortName;

    private MedicineLine line;

    private MedicineCategory category;

    /**
     * Substances abbrevName separated by commas, so they will be related to this medicine
     */
    private String substancesAbbrevName;

    private String customId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public MedicineLine getLine() {
        return line;
    }

    public void setLine(MedicineLine line) {
        this.line = line;
    }

    public MedicineCategory getCategory() {
        return category;
    }

    public void setCategory(MedicineCategory category) {
        this.category = category;
    }

    public String getSubstancesAbbrevName() {
        return substancesAbbrevName;
    }

    public void setSubstancesAbbrevName(String substancesAbbrevName) {
        this.substancesAbbrevName = substancesAbbrevName;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }
}
