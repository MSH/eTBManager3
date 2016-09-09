package org.msh.etbm.commons.indicators.filters;

import org.msh.etbm.commons.indicators.ReportElement;
import org.msh.etbm.commons.indicators.query.SQLDefs;

import java.util.List;

public interface Filter extends ReportElement {

    /**
     * Prepare the query to apply the filters
     * @param def the object to enter SQL information
     * @param comp the filter operation
     * @param value the value to be applied to the filter
     */
    void prepareFilterQuery(SQLDefs def, FilterOperation comp, ValueHandler value);

    /**
     * Return a String identification of the filter's type
     * @return filter's type
     */
    String getFilterType();

    /**
     * Return the options of the filter, or null, if no option is available for this filter
     * @param param is a parameter recognized by the filter to return specific options
     * @return instance of the {@link List} interface containing the {@link FilterOperation}
     */
    List<FilterOption> getFilterOptions(Object param);

    /**
     * Identifies if the options of the filter will be initialized just on demand
     * or filled immediately when the filter is created
     * @return
     */
    boolean isFilterLazyInitialized();

    /**
     * True if the filter accept multiple selection of values
     * @return boolean value
     */
    boolean isMultiSelection();

}
