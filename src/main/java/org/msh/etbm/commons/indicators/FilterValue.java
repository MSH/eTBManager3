package org.msh.etbm.commons.indicators;

import org.msh.etbm.commons.indicators.filters.Filter;
import org.msh.etbm.commons.indicators.filters.FilterOperation;

public class FilterValue {

    private Filter filter;
    private FilterOperation comparator;
    private String value;

    public FilterValue(Filter filter, FilterOperation comparator, String value) {
        super();
        this.filter = filter;
        this.comparator = comparator;
        this.value = value;
    }

    /**
     * @return the filter
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * @return the comparator
     */
    public FilterOperation getComparator() {
        return comparator;
    }

    /**
     * @param comparator the comparator to set
     */
    public void setComparator(FilterOperation comparator) {
        this.comparator = comparator;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
