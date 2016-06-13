package org.msh.etbm.services.session.search;

import org.msh.etbm.db.enums.SearchableType;

import javax.validation.constraints.NotNull;

/**
 * Contain request information to be sent to the search service
 * Created by rmemoria on 11/6/16.
 */
public class SearchRequest {

    /**
     * The key to be used as a search criteria for the title name
     */
    @NotNull
    private String key;

    /**
     * Max number of results
     */
    private Integer maxResults;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }
}
