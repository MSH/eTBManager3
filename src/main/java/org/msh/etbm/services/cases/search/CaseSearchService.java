package org.msh.etbm.services.cases.search;

import org.msh.etbm.services.cases.filters.FilterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to provide advanced search of cases, using filters to narrow the result
 *
 * Created by rmemoria on 17/8/16.
 */
@Service
public class CaseSearchService {

    @Autowired
    FilterManager filterManager;

    public CaseSearchInitResponse init() {
        CaseSearchInitResponse resp = new CaseSearchInitResponse();

        resp.setFilters(filterManager.getFiltersData());

        return resp;
    }
}
