package org.msh.etbm.services.admin.admunits;

import java.util.Optional;

/**
 * Created by rmemoria on 24/10/15.
 */
public class CountryStructureRequest {

    /**
     * Available sorting options
     */
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_LEVEL = "level, name";
    public static final String ORDERBY_LEVEL_DESC = "level desc, name desc";

    private Optional<String> name;

    private Optional<Integer> level;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<Integer> getLevel() {
        return level;
    }

    public void setLevel(Optional<Integer> level) {
        this.level = level;
    }
}
