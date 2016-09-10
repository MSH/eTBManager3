package org.msh.etbm.commons.indicators;


import org.msh.etbm.commons.filters.Filter;

/**
 * Represent an element of the report, which may be a {@link Filter}
 * or a {@link org.msh.etbm.commons.indicators.variables.Variable}
 *
 * @author Ricardo Memoria
 *
 */
public interface ReportElement {

    /**
     * Internal ID of the element
     * @return
     */
    String getId();

    /**
     * Display name of the report element
     * @return
     */
    String getLabel();

}
