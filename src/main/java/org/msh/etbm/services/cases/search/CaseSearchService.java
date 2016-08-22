package org.msh.etbm.services.cases.search;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.services.cases.filters.Filter;
import org.msh.etbm.services.cases.filters.FilterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to provide advanced search of cases, using filters to narrow the result
 *
 * Created by rmemoria on 17/8/16.
 */
@Service
public class CaseSearchService {

    @Autowired
    FilterManager filterManager;

    @Autowired
    DataSource dataSource;

    public CaseSearchInitResponse init() {
        CaseSearchInitResponse resp = new CaseSearchInitResponse();

        resp.setFilters(filterManager.getFiltersData());

        return resp;
    }


    public QueryResult<CaseData> searchCases(CaseSearchRequest req) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);

        SQLQueryBuilder builder = new SQLQueryBuilder("tbcase");

        // limit the number of records to return
        int maxResult = req.getPageSize() != null ? req.getPageSize() : 50;
        Integer firstResult = req.getPage() != null ? req.getPage() * maxResult : null;

        builder.setFirstResult(firstResult);
        builder.setMaxResult(maxResult);

        // get fields to be selected
        builder.select("id, registrationNumber, caseNumber, state, classification, diagnosisType")
                .join("patient", "$this.id = tbcase.patient_id")
                .select("name, middleName, lastName, gender")
                .join("unit", "$this.id = tbcase.owner_unit_id")
                .select("id, name, active")
                .join("administrativeunit", "$this.id = $parent.id")
                .select("id, name, pid0, pid1, pid2, pid3, pname0, pname1, pname2, pname3");

        Map<Filter, Object> filterValues = resolveFilters(req.getFilters());

        for (Map.Entry<Filter, Object> entry: filterValues.entrySet()) {
            Filter filter = entry.getKey();
            Object value = entry.getValue();

            filter.prepareFilterQuery(builder, value, null);
        }

        builder.setOrderBy("patient.name");

        String sql = builder.generate();

        System.out.println(sql);

        List<CaseData> lst = template.query(sql, builder.getParameters(), new RowMapper<CaseData>() {
            @Override
            public CaseData mapRow(ResultSet resultSet, int i) throws SQLException {
                return rowToCaseData(resultSet);
            }
        });

        return new QueryResult<>(0, lst);
    }


    protected CaseData rowToCaseData(ResultSet resultSet) {
        CaseData data = new CaseData();

        return data;
    }


    /**
     * Convert filters ID to its corresponding filter object
     * @param filters the list of filter IDs and its values
     * @return the map of filters and its values
     */
    private Map<Filter, Object> resolveFilters(Map<String, Object> filters) {
        Map<Filter, Object> res = new HashMap<>();

        if (filters != null) {
            for (Map.Entry<String, Object> entry: filters.entrySet()) {
                Filter filter = filterManager.filterById(entry.getKey());
                if (filter == null) {
                    throw new InvalidArgumentException("Filter not found: " + entry.getKey());
                }
                res.put(filter, entry.getValue());
            }
        }

        return res;
    }
}
