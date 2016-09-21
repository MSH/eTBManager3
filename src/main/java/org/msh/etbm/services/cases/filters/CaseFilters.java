package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.filters.FilterData;
import org.msh.etbm.commons.filters.FilterGroupData;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.InfectionSite;
import org.msh.etbm.services.cases.filters.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 17/9/16.
 */
@Service
public class CaseFilters {

    public static final String CASE_STATE = "case-state";
    public static final String CASE_CLASSIFICATION = "classification";
    public static final String INFECTION_SITE = "infection-site";
    public static final String DIAGNOSIS_TYPE = "diagnosis-type";
    public static final String NATIONALITY = "nationality";
    public static final String GENDER = "gender";
    public static final String OUTCOME = "outcome";
    public static final String REGISTRATION_GROUP = "registration-group";
    public static final String DRUG_RESISTANCE_TYPE = "drug-resistance-type";
    public static final String PULMONARY_FORMS = "pulmonary-forms";
    public static final String EXTRAPULMONARY_FORMS = "extrapulmonary-forms";
    public static final String TAG = "tag";
    public static final String SUMMARY = "summary";


    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Messages messages;


    public Map<String, Map<String, Filter>> filterGroups;


    protected void createFilters() {
        if (filterGroups != null) {
            return;
        }

        filterGroups = new HashMap<>();

        createCaseDataFilters(createGroup("cases.details.case"));
        createMicroscopyFilters(createGroup("cases.exammicroscopy"));
        createCultureFilters(createGroup("cases.examculture"));
        createOtherFilters(createGroup("cases.filters.others"));

        initFilters();
    }

    private void createCaseDataFilters(Map<String, Filter> filters) {
        filters.put(CASE_CLASSIFICATION, new EnumFilter(
                CaseClassification.class, "${CaseClassification}", "tbcase.classification"));

        filters.put(GENDER, new GenderFilter());

        filters.put(CASE_STATE, new EnumFilter(
                CaseState.class, "${CaseState}", "tbcase.state"));

        filters.put(INFECTION_SITE, new EnumFilter(
                InfectionSite.class, "${InfectionSite}", "tbcase.infectionSite"));

        filters.put(DIAGNOSIS_TYPE, new EnumFilter(
                DiagnosisType.class, "${DiagnosisType}", "tbcase.diagnosisType"));

        filters.put(NATIONALITY, new ModelFieldOptionsFilter(
                "${Nationality}", "tbcase", "nationality"));

        filters.put(OUTCOME, new ModelFieldOptionsFilter(
                "${TbCase.outcome}", "tbcase", "outcome"));

        filters.put(REGISTRATION_GROUP, new ModelFieldOptionsFilter(
                "${TbCase.registrationGroup}", "tbcase", "registrationGroup"));

        filters.put(DRUG_RESISTANCE_TYPE, new ModelFieldOptionsFilter(
                "${DrugResistanceType}", "tbcase", "drugResistanceType"));

        filters.put(PULMONARY_FORMS, new ModelFieldOptionsFilter(
                "${TbCase.pulmonaryType}", "tbcase", "pulmonaryType"));

        filters.put(EXTRAPULMONARY_FORMS, new ModelFieldOptionsFilter(
                "${TbCase.extrapulmonaryType}", "tbcase", "extrapulmonaryType"));

    }

    private void createMicroscopyFilters(Map<String, Filter> group) {

    }

    private void createCultureFilters(Map<String, Filter> group) {

    }

    private void createOtherFilters(Map<String, Filter> filters) {
        filters.put(TAG, new TagFilter());
        filters.put(SUMMARY, new SummaryFilter());
    }

    private Map<String, Filter> createGroup(String messageKey) {
        Map<String, Filter> res = new HashMap<>();
        filterGroups.put(messageKey, res);
        return res;
    }

    /**
     * Initialize the filters passing the instance of ApplicationContext to allow them to
     * interact with spring beans
     */
    private void initFilters() {
        for (Map.Entry<String, Map<String, Filter>> entry: filterGroups.entrySet()) {
            for (Map.Entry<String, Filter> filter: entry.getValue().entrySet()) {
                filter.getValue().initialize(applicationContext);
            }
        }
    }

    /**
     * Search a filter by its ID
     * @param id the filter ID
     * @return the filter that matches the ID
     */
    public Filter filterById(String id) {
        if (filterGroups == null) {
            createFilters();
        }

        for (Map.Entry<String, Map<String, Filter>> grp: filterGroups.entrySet()) {
            for (Map.Entry<String, Filter> filter: grp.getValue().entrySet()) {
                if (filter.getKey().equals(id)) {
                    return filter.getValue();
                }
            }
        }

        return null;
    }


    /**
     * Return the list of filters
     * @return
     */
    public List<FilterGroupData> getFiltersData() {
        createFilters();

        List<FilterGroupData> res = new ArrayList<>();

        for (Map.Entry<String, Map<String, Filter>> grp: filterGroups.entrySet()) {
            if (grp.getValue().size() > 0) {
                String grpLabel = messages.get(grp.getKey());

                FilterGroupData grpdata = new FilterGroupData();
                grpdata.setLabel(grpLabel);

                List<FilterData> lst = new ArrayList<>();
                for (Map.Entry<String, Filter> f: grp.getValue().entrySet()) {
                    Filter filter = f.getValue();
                    String label = messages.eval(filter.getLabel());
                    Map<String, Object> resources = filter.getResources(null);
                    FilterData fdata = new FilterData(f.getKey(), label, resources);
                    fdata.setType(filter.getFilterType());

                    lst.add(fdata);
                }

                grpdata.setFilters(lst);

                res.add(grpdata);
            }
        }

        return res;
    }


    /**
     * Convert a filter to its display representation
     * @param filterId
     * @param value
     * @return
     */
    public FilterDisplay filterToDisplay(String filterId, Object value) {
        Filter filter = filterById(filterId);
        if (filter == null) {
            return null;
        }

        String name = messages.eval(filter.getLabel());
        String valueDisplay = filter.valueToDisplay(value);

        FilterDisplay f = new FilterDisplay(name, valueDisplay);

        return f;
    }

}
