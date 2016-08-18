package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.services.cases.filters.impl.CaseClassificationFilter;
import org.msh.etbm.services.cases.filters.impl.ValidationStateFilter;
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
     * Initialize the filters, create the list of available filters for cases
     */
    protected void initFilters() {
        // check if filters were already initialized
        if (filters != null) {
            return;
        }

        // initialize filters
        filters = new ArrayList<>();

        filters.add(new CaseClassificationFilter());
        filters.add(new ValidationStateFilter());
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

            grp.getFilters().add(data);
        }

        return res;
    }
}
