package org.msh.etbm.services.cases.search;

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
     * Optional scope of an administrative unit
     */
    private UUID adminUnitId;

    /**
     * Optional scope of a TB unit
     */
    private UUID unitId;

    /**
     * List of filter IDs and its values
     */
    private Map<String, Object> filters;


    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
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
}
