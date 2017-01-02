package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.filters.FilterData;
import org.msh.etbm.commons.filters.FilterGroupData;
import org.msh.etbm.commons.filters.FilterItem;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.indicators.variables.VariableData;
import org.msh.etbm.commons.indicators.variables.VariableGroupData;
import org.msh.etbm.commons.indicators.variables.VariableOutput;
import org.msh.etbm.db.enums.*;
import org.msh.etbm.services.cases.filters.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provide access to all filters and variables of the case module
 *
 * Created by rmemoria on 17/9/16.
 */
@Service
public class CaseFilters {

    public static final VariableOutput VAROUT_CASES = new VariableOutput("cases", "${cases}");
    public static final VariableOutput VAROUT_EXAMS = new VariableOutput("exams", "${exams}");

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
    public static final String CASE_DEFINITION = "case-definition";
    public static final String AGE_RANGE = "age-range";
    public static final String HIV_RESULT = "hiv-result";
    public static final String CULTURE_RESULT_GROUP = "culture-res-group";
    public static final String DIAGNOSIS_DATE = "diagnosis-date";
    public static final String INI_TREATMENT_DATE = "ini-treat-date";
    public static final String END_TREATMENT_DATE = "end-treat-date";
    public static final String OUTCOME_DATE = "outcome-date";
    public static final String MONTH_OF_TREAT = "month-of-treat";
    public static final String PRESC_MEDICINE = "presc-medicine";

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Messages messages;


    public List<FilterGroup> groups;


    /**
     * Declare the filters and variables of the case data group
     * @param grp
     */
    private void createCaseDataFilters(FilterGroup grp) {
        grp.add(new EnumFilter(CASE_CLASSIFICATION,
                CaseClassification.class, "${CaseClassification}", "tbcase.classification"));

        grp.add(new ModelFieldOptionsFilter(GENDER, "${Gender}", "patient", "gender"));

        grp.add(new EnumFilter(DIAGNOSIS_TYPE, DiagnosisType.class, "${DiagnosisType}", "tbcase.diagnosisType"));

        grp.add(new EnumFilter(CASE_STATE, CaseState.class, "${CaseState}", "tbcase.state"));

        grp.add(new EnumFilter(INFECTION_SITE, InfectionSite.class, "${InfectionSite}", "tbcase.infectionSite"));

        grp.add(new ModelFieldOptionsFilter(NATIONALITY, "${Nationality}", "tbcase", "nationality"));

        grp.add(new ModelFieldOptionsFilter(REGISTRATION_GROUP,
                "${TbCase.registrationGroup}", "tbcase", "registrationGroup"));

        grp.add(new ModelFieldOptionsFilter(DRUG_RESISTANCE_TYPE,
                "${DrugResistanceType}", "tbcase", "drugResistanceType"));

        grp.add(new ModelFieldOptionsFilter(PULMONARY_FORMS,
                "${TbCase.pulmonaryType}", "tbcase", "pulmonaryType"));

        grp.add(new ModelFieldOptionsFilter(EXTRAPULMONARY_FORMS,
                "${TbCase.extrapulmonaryType}", "tbcase", "extrapulmonaryType"));

        grp.add(new EnumFilter(CASE_DEFINITION, CaseDefinition.class, "${CaseDefinition}", "tbcase.caseDefinition"));

        grp.add(new AgeRangeFilter());

        grp.add(new PeriodFilter(DIAGNOSIS_DATE, "${TbCase.diagnosisDate}", "tbcase.diagnosisDate", PeriodFilter.PeriodVariableType.MONTHLY));

    }

    /**
     * Declare the filters and variables of the treatment group
     * @param grp the group to include the filters
     */
    private void createTreatmentFilters(FilterGroup grp) {
        grp.add(new ModelFieldOptionsFilter(OUTCOME, "${TbCase.outcome}", "tbcase", "outcome"));

        grp.add(new PeriodFilter(INI_TREATMENT_DATE, "${TbCase.iniTreatmentDate}", "tbcase.iniTreatmentDate",
                PeriodFilter.PeriodVariableType.MONTHLY));

        grp.add(new PeriodFilter(END_TREATMENT_DATE, "${TbCase.endTreatmentDate}", "tbcase.endTreatmentDate",
                PeriodFilter.PeriodVariableType.MONTHLY));

        grp.add(new PeriodFilter(OUTCOME_DATE, "${TbCase.outcomeDate}", "tbcase.outcomeDate",
                PeriodFilter.PeriodVariableType.MONTHLY));

        grp.add(new MonthOfTreatFilter());

        grp.add(new PrescribedMedicineFilter());
    }

    private void createHivGroup(FilterGroup grp) {
        grp.add(new HIVResultFilter());
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

    protected void add(Map<String, Filter> filters, FilterItem filter) {
        filters.put(filter.getId(), filter);
    }

    /**
     * Search a filter by its ID
     * @param id the filter ID
     * @return the filter that matches the ID
     */
    public FilterItem filterById(String id) {
        createFiltersVariables();

        for (FilterGroup grp: groups) {
            for (FilterItem filter: grp.getFilters()) {
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
                for (FilterItem filter: grp.getFilters()) {
                    String label = messages.eval(filter.getName());
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
                    vgd.setLabel(messages.eval(grp.getLabel()));

                    vgd.setVariables(grp.getVariables().stream()
                        .map(v -> new VariableData(v.getId(), v.getName(),
                                v.isGrouped(),
                                v.isTotalEnabled()))
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
        createHivGroup(createGroup("${cases.examhiv}"));
        createOtherFilters(createGroup("${cases.filters.others}"));
        createTreatmentFilters(createGroup("${cases.details.treatment}"));

        initFilters();
    }


    /**
     * Convert a filter to its display representation
     * @param filterId
     * @param value
     * @return
     */
    public FilterDisplay filterToDisplay(String filterId, Object value) {
        FilterItem filter = filterById(filterId);
        if (filter == null) {
            return null;
        }

        String name = messages.eval(filter.getName());
        String valueDisplay = filter.valueToDisplay(value);

        FilterDisplay f = new FilterDisplay(name, valueDisplay);

        return f;
    }

}
