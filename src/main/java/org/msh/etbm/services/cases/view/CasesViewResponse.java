package org.msh.etbm.services.cases.view;

import java.util.List;

/**
 * Data used to create the workspace view of the system
 * Created by rmemoria on 17/6/16.
 */
public class CasesViewResponse {

    /**
     * List of places (admin units and units) with quantity of cases
     */
    private List<PlaceData> places;

    public List<PlaceData> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceData> places) {
        this.places = places;
    }
}
