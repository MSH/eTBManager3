package org.msh.etbm.services.admin.admunits;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by rmemoria on 24/10/15.
 */
public class CountryStructureRequest {

    @NotNull
    private String name;

    @Max(5)
    @Min(1)
    private int level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
