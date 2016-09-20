package org.msh.etbm.services.cases.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.commons.JsonParserException;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.search.CaseData;
import org.msh.etbm.services.cases.search.CaseSearchRequest;
import org.msh.etbm.services.cases.search.CaseSearchService;
import org.msh.etbm.services.cases.search.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generate a report summary
 * Created by rmemoria on 17/9/16.
 */
@Service
public class SummaryService {

    @Autowired
    CaseSearchService caseSearchService;

    @Autowired
    Messages messages;

    @Value("${development:false}")
    boolean development;

    private List<SummaryItem> items;


    /**
     * Generate the report summary
     * @param req
     * @return list of {@link SummaryReportData} with the summary report
     */
    public List<SummaryReportData> generateSummary(SummaryRequest req) {
        if (development) {
            // force reloading the summary.json on every report generation
            items = null;
        }

        List<SummaryReportData> summary = new ArrayList<>();

        for (SummaryItem item: getItems()) {
            SummaryReportData data = calcSummaryItem(req, item);
            summary.add(data);
        }

        return summary;
    }

    /**
     * Calculate the number of cases for a summary item
     * @param req the instance of {@link SummaryRequest} submitted by the client
     * @param item the instance of {@link SummaryItem} with filters to apply to the query
     * @return instance of {@link SummaryReportData}
     */
    private SummaryReportData calcSummaryItem(SummaryRequest req, SummaryItem item) {

        Map<String, Object> filters = new HashMap<>();

        filters.putAll(item.getFilters());

        CaseSearchRequest csreq = new CaseSearchRequest();
        csreq.setFilters(filters);
        csreq.setResultType(ResultType.COUNT_ONLY);
        csreq.setScope(req.getScope());
        csreq.setScopeId(req.getScopeId());

        QueryResult<CaseData> res = caseSearchService.searchCases(csreq);

        String name = messages.eval(item.getName());

        return new SummaryReportData(item.getId(), name, res.getCount());
    }

    /**
     * Return the items of the summary report
     * @return list of {@link SummaryItem} objects
     */
    public List<SummaryItem> getItems() {
        if (items == null) {
            items = loadItems();
        }
        return items;
    }

    /**
     * Load the items of the summary
     * @return list of {@link SummaryItem} objects
     */
    protected List<SummaryItem> loadItems() {
        ClassPathResource res = new ClassPathResource("/templates/json/summary.json");

        try {
            InputStream in = res.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory typeFactory = mapper.getTypeFactory();
            return mapper.readValue(in, typeFactory.constructCollectionType(List.class, SummaryItem.class));
        } catch (Exception e) {
            throw new JsonParserException(e);
        }

    }
}
