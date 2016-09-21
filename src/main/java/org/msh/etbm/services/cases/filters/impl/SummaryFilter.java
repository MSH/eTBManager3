package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.filters.FilterException;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.cases.filters.CaseFilters;
import org.msh.etbm.services.cases.summary.SummaryItem;
import org.msh.etbm.services.cases.summary.SummaryService;
import org.springframework.context.ApplicationContext;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of a {@link Filter} for an specific item of the case summary
 *
 * Created by rmemoria on 21/9/16.
 */
public class SummaryFilter extends AbstractFilter {

    private SummaryService summaryService;
    private CaseFilters filters;

    public SummaryFilter() {
        super("${global.summary}");
    }

    @Override
    public void initialize(ApplicationContext context) {
        super.initialize(context);

        summaryService = context.getBean(SummaryService.class);
        filters = context.getBean(CaseFilters.class);
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        if (value == null) {
            return;
        }

        String summaryId = value.toString();

        Optional<SummaryItem> res = summaryService.getItems().stream()
                .filter(f -> f.getId().equals(summaryId))
                .findFirst();

        if (!res.isPresent()) {
            throw new FilterException("Invalid filter value: " + summaryId);
        }

        SummaryItem item = res.get();

        for (Map.Entry<String, Object> fvalue: item.getFilters().entrySet()) {
            Filter filter = filters.filterById(fvalue.getKey());
            filter.prepareFilterQuery(def, fvalue.getValue(), null);
        }
    }

    @Override
    public String getFilterType() {
        return "select";
    }

    @Override
    public List<Item> getOptions() {
        List<SummaryItem> lst = summaryService.getItems();

        Messages messages = getMessages();

        List<Item> options = lst.stream()
                .map(it -> new Item(it.getId(), messages.eval(it.getName())))
                .collect(Collectors.toList());

        return options;
    }

}
