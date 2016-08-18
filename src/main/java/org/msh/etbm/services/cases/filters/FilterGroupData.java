package org.msh.etbm.services.cases.filters;

import java.util.List;

/**
 * Created by rmemoria on 17/8/16.
 */
public class FilterGroupData {

    private String label;

    private List<FilterData> filters;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<FilterData> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterData> filters) {
        this.filters = filters;
    }
}
