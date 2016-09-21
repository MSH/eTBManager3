package org.msh.etbm.services.cases.search;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.filters.FilterDisplay;

import java.util.List;
import java.util.Map;

/**
 * The responde from the {@link CaseSearchService}
 *
 * Created by rmemoria on 21/9/16.
 */
public class CaseSearchResponse extends QueryResult<CaseData> {

    /**
     * A description of the filters and values used in the search, suitable
     * for being displayed to the user
     */
    private Map<String, FilterDisplay> filters;

    public Map<String, FilterDisplay> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, FilterDisplay> filters) {
        this.filters = filters;
    }
}
