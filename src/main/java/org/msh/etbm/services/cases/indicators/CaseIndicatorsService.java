package org.msh.etbm.services.cases.indicators;

import org.apache.commons.collections.map.HashedMap;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.filters.FilterGroupData;
import org.msh.etbm.commons.indicators.IndicatorGenerator;
import org.msh.etbm.commons.indicators.IndicatorRequest;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;
import org.msh.etbm.commons.indicators.indicator.client.IndicatorDataConverter;
import org.msh.etbm.commons.indicators.variables.VariableGroupData;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.services.cases.filters.CaseFilters;
import org.msh.etbm.services.cases.filters.impl.ScopeFilter;
import org.msh.etbm.services.cases.filters.impl.ScopeFilterValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates case indicators
 *
 * Created by rmemoria on 10/9/16.
 */
@Service
public class CaseIndicatorsService {

    @Autowired
    CaseFilters caseFilters;

    @Autowired
    DataSource dataSource;

    @Autowired
    Messages messages;

    @Autowired
    IndicatorGenerator indicatorGenerator;


    /**
     * Generate initialization data for a client to request indicators
     * @return
     */
    public CaseIndicatorInitResponse getInitData() {
        List<FilterGroupData> filters = caseFilters.getFiltersData();
        List<VariableGroupData> variables = caseFilters.getVariablesData();

        CaseIndicatorInitResponse res = new CaseIndicatorInitResponse();
        res.setFilters(filters);
        res.setVariables(variables);

        return res;
    }

    /**
     * Generate a new case indicator based on the request
     * @param req the instance of {@link CaseIndicatorRequest} containing the indicator request
     * @return return the indicator data
     */
    public CaseIndicatorResponse execute(@Valid CaseIndicatorRequest req) {
        IndicatorRequest indReq = new IndicatorRequest();

        SQLQueryBuilder builder = new SQLQueryBuilder("tbcase");

        // add named joins to be used throughout the queries
        builder.addNamedJoin("patient", "patient", "patient.id = tbcase.patient_id");
        builder.addNamedJoin("ownerUnit", "unit", "$this.id = tbcase.owner_unit_id");
        builder.addNamedJoin("ownerAdminUnit", "administrativeunit", "$this.id = ownerUnit.adminunit_id");
        builder.addNamedJoin("notifUnit", "unit", "$this.id = tbcase.notification_unit_id");
        builder.addNamedJoin("regimen", "regimen", "$this.id = tbcase.regimen_id");
        builder.addNamedJoin("prescribedmedicine", "prescribedmedicine", "$this.case_id = tbcase.id");
        indReq.setQueryBuilder(builder);

        // get list of column variables
        if (req.getColumnVariables() != null) {
            indReq.setColumnVariables(req.getColumnVariables()
                .stream()
                .map(vname -> caseFilters.variableById(vname))
                .collect(Collectors.toList()));
        }

        // get list of row variables
        if (req.getRowVariables() != null) {
            indReq.setRowVariables(req.getRowVariables()
                .stream()
                .map(vname -> caseFilters.variableById(vname))
                .collect(Collectors.toList()));
        }

        // set the filters
        Map<Filter, Object> fvalues = new HashedMap();

        // add restrictions by scope
        addScopeRestriction(fvalues, req);

        if (req.getFilters() != null) {
            for (Map.Entry<String, Object> entry: req.getFilters().entrySet()) {
                Filter filter = caseFilters.filterById(entry.getKey());
                if (filter == null) {
                    throw new EntityValidationException(req, "filters", "Filter not found: " + entry.getKey(), null);
                }
                fvalues.put(filter, entry.getValue());
            }
        }
        indReq.setFilterValues(fvalues);

        indReq.setColumnTotal(true);
        indReq.setRowTotal(true);

        IndicatorDataTable indDataTable = indicatorGenerator.execute(indReq, dataSource, messages);

        IndicatorData data = IndicatorDataConverter.convertFromDataTableIndicator(indDataTable);

        CaseIndicatorResponse resp = new CaseIndicatorResponse();
        resp.setIndicator(data);

        return resp;
    }

    /**
     * Restrict the data by the administrative unit and its children
     * @param filterValues
     * @param req
     */
    private void addScopeRestriction(Map<Filter, Object> filterValues, CaseIndicatorRequest req) {
        ScopeFilterValue val = new ScopeFilterValue(req.getScope(), req.getScopeId(),
                "$root", "$root.owner_unit_id", "ownerAdminUnit");

        // fixed filter to restrict view by workspace
        filterValues.put(new ScopeFilter(), val);
    }
}
