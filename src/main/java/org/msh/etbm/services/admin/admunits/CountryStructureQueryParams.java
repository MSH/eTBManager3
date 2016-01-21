package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.query.EntityQuery;

/**
 * Extended query for searching of country structure entities
 * Created by rmemoria on 28/10/15.
 */
public class CountryStructureQueryParams extends EntityQuery {
    /**
     * The level to search for
     */
    private Integer level;

    /**
     * The name to search for
     */
    private String name;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
