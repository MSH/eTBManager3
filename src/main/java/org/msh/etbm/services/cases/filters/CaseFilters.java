package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.filters.FilterData;
import org.msh.etbm.commons.filters.FilterGroupData;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.indicators.variables.VariableData;
import org.msh.etbm.commons.indicators.variables.VariableGroupData;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.InfectionSite;
import org.msh.etbm.services.cases.filters.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public List<FilterGroup> groups;


    private void createCaseDataFilters(FilterGroup grp) {

        grp.add(new EnumFilter(CASE_CLASSIFICATION,
                CaseClassification.class, "${CaseClassification}", "tbcase.classification"));
        grp.add(new GenderFilter());
        grp.add(new EnumFilter(CASE_STATE, CaseState.class, "${CaseState}", "tbcase.state"));
        grp.add(new EnumFilter(INFECTION_SITE, InfectionSite.class, "${InfectionSite}", "tbcase.infectionSite"));
        grp.add(new EnumFilter(DIAGNOSIS_TYPE, DiagnosisType.class, "${DiagnosisType}", "tbcase.diagnosisType"));
        grp.add(new ModelFieldOptionsFilter(NATIONALITY, "${Nationality}", "tbcase", "nationality"));
        grp.add(new ModelFieldOptionsFilter(OUTCOME, "${TbCase.outcome}", "tbcase", "outcome"));
        grp.add(new ModelFieldOptionsFilter(REGISTRATION_GROUP,
                "${TbCase.registrationGroup}", "tbcase", "registrationGroup"));
        grp.add(new ModelFieldOptionsFilter(DRUG_RESISTANCE_TYPE,
                "${DrugResistanceType}", "tbcase", "drugResistanceType"));
        grp.add(new ModelFieldOptionsFilter(PULMONARY_FORMS,
                "${TbCase.pulmonaryType}", "tbcase", "pulmonaryType"));
        grp.add(new ModelFieldOptionsFilter(EXTRAPULMONARY_FORMS,
                "${TbCase.extrapulmonaryType}", "tbcase", "extrapulmonaryType"));
    }

    private void createMicroscopyFilters(FilterGroup grp) {

    }

    private void createCultureFilters(FilterGroup grp) {

    }

    private void createOtherFilters(FilterGroup grp) {
        grp.add(new TagFilter());
        grp.add(new SummaryFilter());
    }

    private FilterGroup createGroup(String messageKey) {
        FilterGroup grp = new FilterGroup(messageKey);
        groups.add(grp);
        return grp;
    }

    /**
     * Initialize the filters passing the instance of ApplicationContext to allow them to
     * interact with spring beans
     */
    private void initFilters() {
        groups.stream().forEach(grp -> {
            grp.getFilters().forEach(filter -> filter.initialize(applicationContext));
            grp.getVariables().forEach(variable -> variable.initialize(applicationContext));
        });
    }

    protected void add(Map<String, Filter> filters, Filter filter) {
        filters.put(filter.getId(), filter);
    }

    /**
     * Search a filter by its ID
     * @param id the filter ID
     * @return the filter that matches the ID
     */
    public Filter filterById(String id) {
        createFiltersVariables();

        for (FilterGroup grp: groups) {
            for (Filter filter: grp.getFilters()) {
                if (filter.getId().equals(id)) {
                    return filter;
                }
            }
        }

        return null;
    }

    /**
     * Search for a variable by its id
     * @param id the variable ID
     * @return
     */
    public Variable variableById(String id) {
        createFiltersVariables();

        for (FilterGroup grp: groups) {
            for (Variable var: grp.getVariables()) {
                if (var.getId().equals(id)) {
                    return var;
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
        createFiltersVariables();

        List<FilterGroupData> res = new ArrayList<>();

        for (FilterGroup grp: groups) {
            if (grp.getFilters().size() > 0) {
                String grpLabel = messages.eval(grp.getLabel());

                FilterGroupData grpdata = new FilterGroupData();
                grpdata.setLabel(grpLabel);

                List<FilterData> lst = new ArrayList<>();
                for (Filter filter: grp.getFilters()) {
                    String label = messages.eval(filter.getLabel());
                    Map<String, Object> resources = filter.getResources(null);
                    FilterData fdata = new FilterData(filter.getId(), label, resources);
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
     * Return variable information ready for client serialization
     * @return List of {@link VariableGroupData} containing the variable groups
     */
    public List<VariableGroupData> getVariablesData() {
        createFiltersVariables();

        List<VariableGroupData> res = groups.stream()
                .filter(grp -> grp.getVariables() != null && grp.getVariables().size() > 0)
                .map(grp -> {
                    VariableGroupData vgd = new VariableGroupData();
                    vgd.setLabel(grp.getLabel());

                    vgd.setVariables(grp.getVariables().stream()
                        .map(v -> new VariableData(v.getId(), v.getLabel(),
                                v.getVariableOptions().isGrouped(),
                                v.getVariableOptions().isTotalEnabled()))
                        .collect(Collectors.toList()));

                    return vgd;
                })
                .collect(Collectors.toList());

        return res;
    }

    protected void createFiltersVariables() {
        if (groups != null) {
            return;
        }

        groups = new ArrayList<>();

        createCaseDataFilters(createGroup("${cases.details.case}"));
        createMicroscopyFilters(createGroup("${cases.exammicroscopy}"));
        createCultureFilters(createGroup("${cases.examculture}"));
        createOtherFilters(createGroup("${cases.filters.others}"));

        initFilters();
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
