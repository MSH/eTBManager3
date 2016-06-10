package org.msh.etbm.services.session.search;

import java.util.UUID;

/**
 * Store information about an item returned from the search service of the system
 * Created by rmemoria on 9/6/16.
 */
public class SearchResponse {

    /**
     * The ID of the item
     */
    private UUID id;

    /**
     * The type of information returned in this item (unit, case, suspect, etc)
     */
    private SearchResponseType type;

    /**
     * The title to be displayed about this item
     */
    private String title;

    /**
     * Subtitle (if applicable) to be displayed under the title
     */
    private String subtitle;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SearchResponseType getType() {
        return type;
    }

    public void setType(SearchResponseType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
