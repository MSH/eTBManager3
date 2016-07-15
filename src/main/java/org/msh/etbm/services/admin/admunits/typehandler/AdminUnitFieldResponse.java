package org.msh.etbm.services.admin.admunits.typehandler;

import org.msh.etbm.services.admin.admunits.AdminUnitItemData;

import java.util.List;
import java.util.UUID;

/**
 * Response sent to the client in order to initialize a field of an administrative unit selection box
 * <p>
 * Created by rmemoria on 18/1/16.
 */
public class AdminUnitFieldResponse {

    /**
     * The level of the administrative unit list
     */
    private int level;

    /**
     * The label assigned to the list (name of the country structure in the level)
     */
    private String label;

    /**
     * List of options for the given level
     */
    private List<AdminUnitItemData> list;

    /**
     * Selected administrative unit in the level
     */
    private UUID selected;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<AdminUnitItemData> getList() {
        return list;
    }

    public void setList(List<AdminUnitItemData> list) {
        this.list = list;
    }

    public UUID getSelected() {
        return selected;
    }

    public void setSelected(UUID selected) {
        this.selected = selected;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
