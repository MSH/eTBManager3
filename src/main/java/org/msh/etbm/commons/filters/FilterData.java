package org.msh.etbm.commons.filters;

import java.util.Map;

/**
 * The filter data to be serialized to the client
 *
 * Created by rmemoria on 17/8/16.
 */
public class FilterData {

    /**
     * The unique filter ID, used when doing reference to a filter
     */
    private String id;

    /**
     * The label to be displayed for the user
     */
    private String label;

    /**
     * The type of filter, that determines the control to be used
     */
    private String type;

    /**
     * The resources used by the filter control (type)
     */
    private Map<String, Object> resources;


    public FilterData(String id, String label, Map<String, Object> resources) {
        this.id = id;
        this.label = label;
        this.resources = resources;
    }

    public FilterData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
