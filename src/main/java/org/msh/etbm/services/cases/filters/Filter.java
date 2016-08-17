package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.sqlquery.QueryDefs;

import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 15/8/16.
 */
public interface Filter {

    /**
     * The filter ID, used when making reference to a filter
     * @return
     */
    String getId();


    /**
     * Return the filter's label to be displayed to the user
     * @return
     */
    String getLabel();


    /**
     * Prepare the query to apply the filters
     * @param def the object to enter SQL information
     * @param value the value to be applied to the filter
     * @param params optional parameters sent and interpreted by the filter
     */
    void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params);

    /**
     * Return a String identification of the filter's type
     * @return filter's type
     */
    String getFilterType();

    /**
     * Return the options of the filter, or null, if no option is available for this filter
     * @param param is a parameter recognized by the filter to return specific options
     * @return instance of the {@link List} interface containing the {@link Item}
     */
    List<Item> getFilterOptions(Object param);

    /**
     * True if the filter accept multiple selection of values
     * @return boolean value
     */
    boolean isMultiSelection();

}
