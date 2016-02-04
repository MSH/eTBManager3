package org.msh.etbm.services.admin.admunits;

import java.util.Optional;

/**
 * Created by rmemoria on 24/10/15.
 */
public class CountryStructureFormData {

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
