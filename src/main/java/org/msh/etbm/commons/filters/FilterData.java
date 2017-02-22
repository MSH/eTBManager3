package org.msh.etbm.commons.filters;

import org.msh.etbm.commons.Item;

import java.util.Map;

/**
 * The filter data to be serialized to the client
 *
 * Created by rmemoria on 17/8/16.
 */
public class FilterData extends Item<String> {

    /**
     * The type of filter, that determines the control to be used
     */
    private String type;

    /**
     * The resources used by the filter control (type)
     */
    private Map<String, Object> resources;


    public FilterData(String id, String label, Map<String, Object> resources) {
        super(id, label);
        this.resources = resources;
    }

    public FilterData() {
    }

    public Map<String, Object> getResources() {
        return resources;
    }

    public void setResources(Map<String, Object> resources) {
        this.resources = resources;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
