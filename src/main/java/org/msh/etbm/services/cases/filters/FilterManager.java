package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.InfectionSite;
import org.msh.etbm.services.cases.filters.impl.EnumFilter;
import org.msh.etbm.services.cases.filters.impl.GenderFilter;
import org.msh.etbm.services.cases.filters.impl.ModelFieldOptionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintain a list of filters used in the case management module
 *
 * Created by rmemoria on 17/8/16.
 */
@Service
public class FilterManager {

    @Autowired
    Messages messages;

    @Autowired
    ModelDAOFactory modelDAOFactory;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * Store the list of filters
     */
    private List<Filter> filters;

    public List<Filter> getFilters() {
        // TODO Temporary - Remove filters = null;
        filters = null;
        initFilters();
        return filters;
    }

    /**
     * Search a filter by its ID
     * @param id the filter ID
     * @return the filter that matches the ID
     */
    public Filter filterById(String id) {
        for (Filter filter: getFilters()) {
            if (filter.getId().equals(id)) {
                return filter;
            }
        }

        return null;
    }

    /**
     * Initialize the filters, create the list of available filters for cases
     */
    protected void initFilters() {
        // check if filters were already initialized
        if (filters != null) {
            return;
        }

        // initialize filters
        filters = new ArrayList<>();

        filters.add(new EnumFilter(FilterGroup.DATA, CaseClassification.class,
                "classif", "${CaseClassification}", "tbcase.classification"));

        filters.add(new GenderFilter());

        filters.add(new EnumFilter(FilterGroup.DATA, CaseState.class,
                "case-state", "${CaseState}", "tbcase.state"));

        filters.add(new EnumFilter(FilterGroup.DATA, InfectionSite.class,
                "infection-site", "${InfectionSite}", "tbcase.infectionSite"));

        filters.add(new EnumFilter(FilterGroup.DATA, DiagnosisType.class,
                "diag-type", "${DiagnosisType}", "tbcase.diagnosisType"));

        filters.add(new ModelFieldOptionsFilter(FilterGroup.DATA,
                "nat",
                "${Nationality}",
                "tbcase",
                "nationality"));

        filters.add(new ModelFieldOptionsFilter(FilterGroup.DATA,
                "outc",
                "${TbCase.outcome}",
                "tbcase",
                "outcome"));

        filters.add(new ModelFieldOptionsFilter(FilterGroup.DATA,
                "reggroup",
                "${TbCase.registrationGroup}",
                "tbcase",
                "registrationGroup"));

        filters.add(new ModelFieldOptionsFilter(FilterGroup.DATA,
                "drugRegType", // ID
                "${DrugResistanceType}", // label
                "tbcase",   // model
                "drugResistanceType")); // field

        filters.add(new EnumFilter(FilterGroup.DATA,
                InfectionSite.class,
                "inf-site",
                "${InfectionSite}",
                "infectionSite"));

        filters.add(new ModelFieldOptionsFilter(FilterGroup.DATA,
                "pulmonary-forms", // ID
                "${TbCase.pulmonaryType}", // label
                "tbcase",   // model
                "pulmonary")); // field

        filters.add(new ModelFieldOptionsFilter(FilterGroup.DATA,
                "extrapulmonary-forms", // ID
                "${TbCase.extrapulmonaryType}", // label
                "tbcase",   // model
                "extrapulmonary")); // field
//        filters.add(new OutcomeFilter());

//        filters.add(new DummyFilter(FilterGroup.DATA, "notif-addr", "${Address}"));
//        filters.add(new DummyFilter(FilterGroup.DATA, "notif-unit", "${TbCase.notificationUnit}"));
//        filters.add(new DummyFilter(FilterGroup.DATA, "drug-resist-type", "${DrugResistanceType}"));
//        filters.add(new DummyFilter(FilterGroup.DATA, "pulmonary-types", "${TbCase.pulmonaryType}"));
//        filters.add(new DummyFilter(FilterGroup.DATA, "extrapulmonary-types", "${TbCase.extrapulmonaryType}"));
//        filters.add(new DummyFilter(FilterGroup.DATA, "patient-type", "${PatientType}"));
//
//        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "cult-diag", "Culture filter 1"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "culture-res", "Culture filter 2"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "culture-month", "Culture filter 3"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "filter1", "Filter 1"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "filter2", "Filter 2"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "filter3", "Filter 3"));
//
//        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "mic-res", "Microscopy filter 1"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "mic-diag", "Microscopy filter 2"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "mic-month", "Microscopy filter 3"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "filter1", "Filter 1"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "filter2", "Filter 2"));
//        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "filter3", "Filter 3"));

        for (Filter filter: filters) {
            filter.initialize(applicationContext);
        }
    }

    /**
     * Return the list of filters
     * @return
     */
    public List<FilterGroupData> getFiltersData() {
        List<FilterGroupData> res = new ArrayList<>();

        FilterContext context = new FilterContext(true, messages, modelDAOFactory);

        for (Filter filter: getFilters()) {
            String label = messages.get(filter.getGroup().getMessageKey());

            // search for the group in the list
            FilterGroupData grp = res.stream()
                    .filter(item -> item.getLabel().equals(label))
                    .findFirst()
                    .orElse(null);

            if (grp == null) {
                grp = new FilterGroupData();
                grp.setLabel(label);
                grp.setFilters(new ArrayList<>());
                res.add(grp);
            }

            FilterData data = new FilterData(filter.getId(),
                    messages.eval(filter.getLabel()),
                    filter.getResources(context, null));
            data.setType(filter.getFilterType());

            grp.getFilters().add(data);
        }

        return res;
    }
}
