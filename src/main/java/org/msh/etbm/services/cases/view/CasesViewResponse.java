package org.msh.etbm.services.cases.view;

import org.msh.etbm.services.cases.tag.CasesTagsReportItem;

import java.util.List;

/**
 * Data used to create the workspace view of the system
 * Created by rmemoria on 17/6/16.
 */
public class CasesViewResponse {

    /**
     * List of the tags and its quantity per case
     */
    private List<CasesTagsReportItem> tags;

    /**
     * List of places (admin units and units) with quantity of cases
     */
    private List<PlaceData> places;

    public List<CasesTagsReportItem> getTags() {
        return tags;
    }

    public void setTags(List<CasesTagsReportItem> tags) {
        this.tags = tags;
    }

    public List<PlaceData> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceData> places) {
        this.places = places;
    }
}
