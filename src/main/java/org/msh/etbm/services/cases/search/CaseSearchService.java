package org.msh.etbm.services.cases.search;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.sqlquery.RowReader;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.commons.sqlquery.SQLQueryExec;
import org.msh.etbm.db.PersonName;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.Gender;
import org.msh.etbm.services.RequestScope;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.cases.filters.CaseFilters;
import org.msh.etbm.services.cases.filters.FilterDisplay;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    CaseFilters caseFilters;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;


    /**
     * Return the resources to the client in order to query cases
     * @return instance of {@link CaseSearchInitResponse}
     */
    public CaseSearchInitResponse init() {
        CaseSearchInitResponse resp = new CaseSearchInitResponse();

        resp.setFilters(caseFilters.getFiltersData());

        return resp;
    }


    /**
     * Execute the case search
     * @param req the request containing search parameters
     * @return the list of cases found
     */
    public CaseSearchResponse execute(CaseSearchRequest req) {
        CaseSearchResponse res = new CaseSearchResponse();

        List<CaseData> lst = loadCases(req);
        res.setList(lst);

        long count = calcCount(req);
        res.setCount(count);

        // check if it should include displayable data about the filters
        if (req.isAddFilterDisplay()) {
            Map<String, FilterDisplay> filtersDisplay = generateFilterDescriptions(req.getFilters());
            res.setFilters(filtersDisplay);
        }

        return res;
    }


    /**
     * Generate a map with the filter ID and its displayable format
     * @param filters the map with filter ID and its filter value
     */
    protected Map<String, FilterDisplay> generateFilterDescriptions(Map<String, Object> filters) {
        if (filters == null) {
            return null;
        }

        Map<String, FilterDisplay> res = new HashMap<>();

        for (Map.Entry<String, Object> entry: filters.entrySet()) {
            FilterDisplay f = caseFilters.filterToDisplay(entry.getKey(), entry.getValue());
            if (f != null) {
                res.put(entry.getKey(), f);
            }
        }

        return res;
    }

    /**
     * Load the cases based on the request object
     * @param req instance of {@link CaseSearchRequest} containing the criteria
     * @return
     */
    protected List<CaseData> loadCases(CaseSearchRequest req) {
        if (req.getResultType() == ResultType.COUNT_ONLY) {
            return null;
        }

        SQLQueryBuilder builder = createSQLQueryBuilder();

        // limit the number of records to return
        int maxResult = req.getPageSize() != null ? req.getPageSize() : 50;
        Integer firstResult = req.getPage() != null ? req.getPage() * maxResult : null;

        builder.setFirstResult(firstResult);
        builder.setMaxResult(maxResult);

        // get fields to be selected
        builder.select("id, registrationNumber, caseNumber, state, classification, diagnosisType")
                .join("patient", "$this.id = tbcase.patient_id")
                .select("name, middleName, lastName, gender")
                .join("ownerUnit")
                .select("id, name, active")
                .join("ownerAdminUnit")
                .select("id, name, pid0, pid1, pid2, pid3, pname0, pname1, pname2, pname3");

        // apply scope restrictions
        applyScopeRestrictions(req, builder);
        applyFilters(builder, req.getFilters());

        builder.setOrderBy("patient.name");

        SQLQueryExec exec = new SQLQueryExec(dataSource);

        // load cases and fill result
        List<CaseData> lst = exec.query(builder, new org.msh.etbm.commons.sqlquery.RowMapper<CaseData>() {
            @Override
            public CaseData map(RowReader reader) {
                return rowToCaseData(reader);
            }
        });

        return lst;
    }


    /**
     * Apply restrictions to the query according to the scope (workspace, admin unit or unit)
     * @param req instance of {@link CaseSearchRequest}
     * @param builder instance of {@link SQLQueryBuilder}
     */
    protected void applyScopeRestrictions(CaseSearchRequest req, SQLQueryBuilder builder) {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();
        builder.restrict("tbcase.workspace_id = ?", wsId);

        if (req.getScope() == null || req.getScope() == RequestScope.WORKSPACE) {
            return;
        }

        if (req.getScopeId() == null) {
            throw new EntityValidationException(req, "scopeId", null, Messages.NOT_NULL);
        }

        // set the scope by unit
        if (req.getScope() == RequestScope.UNIT) {
            builder.join("ownerUnit")
                    .restrict("ownerUnit.id = ?", req.getScopeId());
            return;
        }

        // assumes that scope is by administrative unit
        AdministrativeUnit adminUnit = entityManager.find(AdministrativeUnit.class, req.getScopeId());
        builder.join("ownerUnit")
                .join("ownerAdminUnit")
                .restrict("(ownerAdminUnit.pid" + adminUnit.getLevel() + " = ? or ownerAdminUnit.id = ?)",
                        req.getScopeId(), req.getScopeId());
    }


    /**
     * Apply the filters in the query builder
     * @param builder
     * @param filters
     */
    protected void applyFilters(SQLQueryBuilder builder, Map<String, Object> filters) {
        Map<Filter, Object> filterValues = resolveFilters(filters);

        // apply filters
        for (Map.Entry<Filter, Object> entry : filterValues.entrySet()) {
            Filter filter = entry.getKey();
            Object value = entry.getValue();

            filter.prepareFilterQuery(builder, value, null);
        }
    }


    /**
     * Create the query builder
     * @return
     */
    protected SQLQueryBuilder createSQLQueryBuilder() {
        SQLQueryBuilder builder = new SQLQueryBuilder("tbcase");

        // add named joins to be used throughout the queries
        builder.addNamedJoin("patient", "patient", "patient.id = tbcase.patient_id");
        builder.addNamedJoin("ownerUnit", "unit", "$this.id = tbcase.owner_unit_id");
        builder.addNamedJoin("ownerAdminUnit", "administrativeunit", "$this.id = ownerUnit.adminunit_id");
        builder.addNamedJoin("notifUnit", "unit", "$this.id = tbcase.notification_unit_id");
        builder.addNamedJoin("regimen", "regimen", "$this.id = tbcase.regimen_id");

        return builder;
    }


    /**
     * Calculate the number of cases returned from the request
     * @return
     */
    protected Long calcCount(CaseSearchRequest req) {
        if (req.getResultType() == ResultType.CASES_ONLY) {
            return null;
        }

        SQLQueryBuilder builder = createSQLQueryBuilder();

        builder.addGroupExpression("count(*)");
        applyScopeRestrictions(req, builder);
        applyFilters(builder, req.getFilters());

        SQLQueryExec exec = new SQLQueryExec(dataSource);
        return exec.queryForObject(builder, Long.class);
    }

    /**
     * Convert a database row data to an object data
     * @param reader the instance of RowReader containing the row values
     * @return instance of {@link CaseData}
     */
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
                Filter filter = caseFilters.filterById(entry.getKey());
                if (filter == null) {
                    throw new InvalidArgumentException("Filter not found: " + entry.getKey());
                }
                res.put(filter, entry.getValue());
            }
        }

        return res;
    }

}
