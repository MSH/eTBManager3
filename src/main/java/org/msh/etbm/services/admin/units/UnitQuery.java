package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.query.EntityQuery;

/**
 * Created by rmemoria on 28/10/15.
 */
public class UnitQuery extends EntityQuery {

    private String name;
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
