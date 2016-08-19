package org.msh.etbm.services.cases.filters;

import java.util.Map;

/**
 * Created by rmemoria on 17/8/16.
 */
public class FilterData {

    private String id;
    private String label;
    private String type;
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
