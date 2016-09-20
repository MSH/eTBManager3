package org.msh.etbm.services.cases.search;

import org.msh.etbm.services.RequestScope;

import java.util.Map;
import java.util.UUID;

/**
 * Request data for case searching
 *
 * Created by rmemoria on 20/8/16.
 */
public class CaseSearchRequest {

    /**
     * The initial paging, considering the search result is paginated. Default value is 0
     */
    private Integer page;

    /**
     * The page size, considering the search result is paginated. Default value is 50
     */
    private Integer pageSize;

    /**
     * The scope of the search, that may be by workspace, admin unit or unit
     */
    private RequestScope scope;

    /**
     * In case the scope is by admin unit or unit, its ID must be specified
     */
    private UUID scopeId;

    /**
     * List of filter IDs and its values
     */
    private Map<String, Object> filters;

    /**
     * Specify what to return in the query, if null it will return both cases and counting
     */
    private ResultType resultType;

    public RequestScope getScope() {
        return scope;
    }

    public void setScope(RequestScope scope) {
        this.scope = scope;
    }

    public UUID getScopeId() {
        return scopeId;
    }

    public void setScopeId(UUID scopeId) {
        this.scopeId = scopeId;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }
}
