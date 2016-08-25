package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.db.enums.*;
import org.msh.etbm.services.cases.filters.impl.DummyFilter;
import org.msh.etbm.services.cases.filters.impl.EnumFilter;
import org.msh.etbm.services.cases.filters.impl.GenderFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Store the list of filters
     */
    private List<Filter> filters;

    public List<Filter> getFilters() {
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

        filters.add(new DummyFilter(FilterGroup.DATA, "nat", "${Nationality}"));
        filters.add(new DummyFilter(FilterGroup.DATA, "notif-addr", "${Address}"));
        filters.add(new DummyFilter(FilterGroup.DATA, "notif-unit", "${TbCase.notificationUnit}"));
        filters.add(new DummyFilter(FilterGroup.DATA, "drug-resist-type", "${DrugResistanceType}"));
        filters.add(new DummyFilter(FilterGroup.DATA, "pulmonary-types", "${TbCase.pulmonaryType}"));
        filters.add(new DummyFilter(FilterGroup.DATA, "extrapulmonary-types", "${TbCase.extrapulmonaryType}"));
        filters.add(new DummyFilter(FilterGroup.DATA, "patient-type", "${PatientType}"));

        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "cult-diag", "Culture filter 1"));
        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "culture-res", "Culture filter 2"));
        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "culture-month", "Culture filter 3"));
        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "filter1", "Filter 1"));
        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "filter2", "Filter 2"));
        filters.add(new DummyFilter(FilterGroup.EXAM_CULTURE, "filter3", "Filter 3"));

        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "mic-res", "Microscopy filter 1"));
        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "mic-diag", "Microscopy filter 2"));
        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "mic-month", "Microscopy filter 3"));
        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "filter1", "Filter 1"));
        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "filter2", "Filter 2"));
        filters.add(new DummyFilter(FilterGroup.EXAM_MICROSCOPY, "filter3", "Filter 3"));
    }

    /**
     * Return the list of filters
     * @return
     */
    public List<FilterGroupData> getFiltersData() {
        List<FilterGroupData> res = new ArrayList<>();

        FilterContext context = new FilterContext(true, messages);

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
