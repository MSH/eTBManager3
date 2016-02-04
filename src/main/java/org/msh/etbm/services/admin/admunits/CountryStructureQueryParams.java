package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

/**
 * Extended query for searching of country structure entities
 * Created by rmemoria on 28/10/15.
 */
public class CountryStructureQueryParams extends EntityQueryParams {
    /**
     * Available sorting options
     */
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_LEVEL = "level";
    public static final String ORDERBY_LEVEL_DESC = "level desc";

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
