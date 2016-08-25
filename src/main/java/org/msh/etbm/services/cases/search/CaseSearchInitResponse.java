package org.msh.etbm.services.cases.search;

import org.msh.etbm.services.cases.filters.FilterGroupData;

import java.util.List;

/**
 * Created by rmemoria on 17/8/16.
 */
public class CaseSearchInitResponse {
    private List<FilterGroupData> filters;

    public List<FilterGroupData> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterGroupData> filters) {
        this.filters = filters;
    }
}
