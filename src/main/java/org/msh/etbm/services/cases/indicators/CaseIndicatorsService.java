package org.msh.etbm.services.cases.indicators;

import org.apache.commons.collections.map.HashedMap;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.IndicatorGenerator;
import org.msh.etbm.commons.indicators.IndicatorRequest;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;
import org.msh.etbm.commons.indicators.indicator.client.IndicatorDataConverter;
import org.msh.etbm.services.cases.filters.CaseFilters;
import org.msh.etbm.services.cases.filters.impl.WorkspaceFilter;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
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
    UserRequestService userRequestService;

    @Autowired
    DataSource dataSource;

    @Autowired
    Messages messages;


    public CaseIndicatorResponse execute(CaseIndicatorRequest req) {
        IndicatorRequest indReq = new IndicatorRequest();
        indReq.setMainTable("tbcase");

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

        // fixed filter to restrict view by workspace
        fvalues.put(new WorkspaceFilter(), userRequestService.getUserSession().getWorkspaceId());

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

        IndicatorGenerator generator = new IndicatorGenerator();

        IndicatorDataTable indDataTable = generator.execute(indReq, dataSource, messages);

        IndicatorData data = IndicatorDataConverter.convertFromDataTableIndicator(indDataTable);

        CaseIndicatorResponse resp = new CaseIndicatorResponse();
        resp.setIndicator(data);

        return resp;
    }
}
