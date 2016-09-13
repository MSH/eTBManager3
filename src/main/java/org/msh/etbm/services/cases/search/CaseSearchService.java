package org.msh.etbm.services.cases.search;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.sqlquery.RowReader;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.commons.sqlquery.SQLQueryExec;
import org.msh.etbm.db.PersonName;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.Gender;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.services.cases.filters.FilterManager;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Autowired
    UserRequestService userRequestService;


    public CaseSearchInitResponse init() {
        CaseSearchInitResponse resp = new CaseSearchInitResponse();

        resp.setFilters(filterManager.getFiltersData());

        return resp;
    }


    public QueryResult<CaseData> searchCases(CaseSearchRequest req) {
        SQLQueryBuilder builder = new SQLQueryBuilder("tbcase");

        // limit the number of records to return
        int maxResult = req.getPageSize() != null ? req.getPageSize() : 50;
        Integer firstResult = req.getPage() != null ? req.getPage() * maxResult : null;

        builder.setFirstResult(firstResult);
        builder.setMaxResult(maxResult);

        // add named joins to be used throughout the queries
        builder.addNamedJoin("patient", "patient", "patient.id = tbcase.id");
        builder.addNamedJoin("ownerUnit", "unit", "$this.id = tbcase.owner_unit_id");
        builder.addNamedJoin("notifUnit", "unit", "$this.id = tbcase.notification_unit_id");
        builder.addNamedJoin("regimen", "regimen", "$this.id = tbcase.regimen_id");

        UUID wsId = userRequestService.getUserSession().getWorkspaceId();

        // get fields to be selected
        builder.select("id, registrationNumber, caseNumber, state, classification, diagnosisType")
                .join("patient", "$this.id = tbcase.patient_id")
                .restrict("tbcase.workspace_id = ?", wsId)
                .select("name, middleName, lastName, gender")
                .join("unit", "$this.id = tbcase.owner_unit_id")
                .select("id, name, active")
                .join("administrativeunit", "$this.id = $parent.adminunit_id")
                .select("id, name, pid0, pid1, pid2, pid3, pname0, pname1, pname2, pname3");

        Map<Filter, Object> filterValues = resolveFilters(req.getFilters());

        for (Map.Entry<Filter, Object> entry: filterValues.entrySet()) {
            Filter filter = entry.getKey();
            Object value = entry.getValue();

            filter.prepareFilterQuery(builder, value, null);
        }
        builder.setOrderBy("patient.name");

        SQLQueryExec exec = new SQLQueryExec(dataSource);

        List<CaseData> lst = exec.query(builder, new org.msh.etbm.commons.sqlquery.RowMapper<CaseData>() {
            @Override
            public CaseData map(RowReader reader) {
                return rowToCaseData(reader);
            }
        });

        int count = calcCount(builder);

        return new QueryResult<>(count, lst);
    }

    protected int calcCount(SQLQueryBuilder builder) {
        builder.clearSelect();
        builder.addGroupExpression("count(*)");
        builder.setMaxResult(null);
        builder.setFirstResult(null);
        builder.setOrderBy(null);

        SQLQueryExec exec = new SQLQueryExec(dataSource);
        return exec.queryForObject(builder, Integer.class);
    }

    protected CaseData rowToCaseData(RowReader reader) {
        CaseData data = new CaseData();

        // read case data
        data.setId(reader.getUUID("id"));
        data.setCaseNumber(reader.getString("caseNumber"));
        data.setClassification(reader.getEnum("classification", CaseClassification.class));
        data.setDiagnosisType(reader.getEnum("diagnosisType", DiagnosisType.class));
        data.setState(reader.getEnum("state", CaseState.class));

        String s = reader.getString("patient.gender");
        Gender g = "MALE".equals(s) ? Gender.MALE : ("FEMALE".equals(s) ? Gender.FEMALE : null);
        data.setGender(g);

        PersonName n = new PersonName();
        n.setName(reader.getString("patient.name"));
        n.setMiddleName(reader.getString("patient.middleName"));
        n.setLastName(reader.getString("patient.lastName"));
        data.setName(n);

        // read unit
        UnitData unit = new UnitData();
        unit.setId(reader.getUUID("unit.id"));
        unit.setName(reader.getString("unit.name"));
        unit.setActive(reader.getBoolean("unit.active"));

        // read administrative unit
        AdminUnitData au = new AdminUnitData();
        au.setId(reader.getUUID("administrativeunit.id"));
        au.setName(reader.getString("administrativeunit.name"));
        au.setP0(new SynchronizableItem(
                reader.getUUID("administrativeunit.pid0"),
                reader.getString("administrativeunit.pname0")));
        au.setP1(new SynchronizableItem(
                reader.getUUID("administrativeunit.pid1"),
                reader.getString("administrativeunit.pname1")));
        au.setP2(new SynchronizableItem(
                reader.getUUID("administrativeunit.pid2"),
                reader.getString("administrativeunit.pname2")));
        au.setP3(new SynchronizableItem(
                reader.getUUID("administrativeunit.pid3"),
                reader.getString("administrativeunit.pname3")));
        unit.setAdminUnit(au);

        data.setUnit(unit);

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
