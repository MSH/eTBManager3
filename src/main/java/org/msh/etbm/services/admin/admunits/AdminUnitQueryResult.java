package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.admunits.data.AdminUnitItemData;

import java.util.List;

/**
 * Extends the query result to, optionally, send information about the country structure to the client
 * Created by rmemoria on 16/12/15.
 */
public class AdminUnitQueryResult extends QueryResult<AdminUnitItemData> {
    /**
     * List of the country structure
     */
    private List<CountryStructureData> csList;

    public List<CountryStructureData> getCsList() {
        return csList;
    }

    public void setCsList(List<CountryStructureData> csList) {
        this.csList = csList;
    }
}
