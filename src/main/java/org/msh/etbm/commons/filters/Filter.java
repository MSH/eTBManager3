package org.msh.etbm.commons.filters;

import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.cases.filters.FilterContext;
import org.msh.etbm.services.cases.filters.FilterGroup;
import org.springframework.context.ApplicationContext;

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
     * Return the filter group related to the
     * @return
     */
    FilterGroup getGroup();

    /**
     * Initialize the filter passing the instance of the ApplicationContext.
     * This is called just once when filter is created and before any other method,
     * in order to give the filter the possibility to get beans
     * @param context instance of ApplicationContext interface
     */
    void initialize(ApplicationContext context);

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
     * Return the resources to be sent to the client in order to display the filter control
     * @param context instance of {@link FilterContext} class, containing other resources that may be used
     *                during creation of filter resources
     * @param params the params sent from the client. Null if called during initialization
     * @return the resources to be sent to the client
     */
    Map<String, Object> getResources(FilterContext context, Map<String, Object> params);
}
