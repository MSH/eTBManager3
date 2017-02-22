package org.msh.etbm.services.cases.summary;

import org.msh.etbm.commons.Item;

import java.util.Map;

/**
 * Define an item of the summary composed of filters and its values. This is a constant value
 * used to generate the summary data
 *
 * Created by rmemoria on 18/9/16.
 */
public class SummaryItem extends Item<String> {

    private Map<String, Object> filters;

    public SummaryItem() {
    }

    public SummaryItem(String id, String name, Map<String, Object> filters) {
        super(id, name);
        this.filters = filters;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
