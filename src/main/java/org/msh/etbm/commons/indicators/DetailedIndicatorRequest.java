package org.msh.etbm.commons.indicators;

import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.List;
import java.util.Map;

/**
 * Request to generate a detailed result of an indicator
 *
 * Created by rmemoria on 10/9/16.
 */
public class DetailedIndicatorRequest extends IndicatorRequest {

    private String detailedFields;
    private Integer firstResult;
    private Integer maxResult;
    private String orderBy;

    public DetailedIndicatorRequest() {
    }

    public DetailedIndicatorRequest(String mainTable, Map<Filter, Object> filterValues,
                                    List<Variable> columnVariables,
                                    List<Variable> rowVariables,
                                    String detailedFields,
                                    Integer firstResult,
                                    Integer maxResult,
                                    String orderBy) {
        super(mainTable, filterValues, columnVariables, rowVariables);
        this.detailedFields = detailedFields;
        this.firstResult = firstResult;
        this.maxResult = maxResult;
        this.orderBy = orderBy;
    }

    public String getDetailedFields() {
        return detailedFields;
    }

    public void setDetailedFields(String detailedFields) {
        this.detailedFields = detailedFields;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    public Integer getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(Integer maxResult) {
        this.maxResult = maxResult;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
