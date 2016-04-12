package org.msh.etbm.commons.entities.query;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * Default query when returning a list of entities
 *
 * Created by rmemoria on 28/10/15.
 */
public class EntityQueryParams {
    /**
     * Page number of the result search. Null indicates all records will be returned
     */
    private Integer page;

    /**
     * Number of records per page. If page number is defined and pageSize is null, the default value is 50
     */
    private Integer pageSize;

    /**
     * Criteria to order list
     */
    private String orderBy;

    /**
     * Specify if order by will be descending
     */
    @JsonProperty("descending")
    private boolean orderByDescending;

    /**
     * Indicate the data to be returned based on the name in profile (entity specific)
     */
    private String profile;

    /**
     * Optionally, the ID can be informed. This way, just one entity is returned
     */
    private UUID id;


    /**
     * List of IDs, when you want to load specific units
     */
    private List<UUID> ids;


    /**
     * if true, just the counting will be performed, and no list will be returned
     */
    private boolean countOnly;

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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isOrderByDescending() {
        return orderByDescending;
    }

    public void setOrderByDescending(boolean orderByDescending) {
        this.orderByDescending = orderByDescending;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<UUID> getIds() {
        return ids;
    }

    public void setIds(List<UUID> ids) {
        this.ids = ids;
    }

    public boolean isCountOnly() {
        return countOnly;
    }

    public void setCountOnly(boolean countOnly) {
        this.countOnly = countOnly;
    }
}
