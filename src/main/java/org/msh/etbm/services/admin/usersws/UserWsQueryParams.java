package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.UUID;

/**
 * Query parameters to search for users of the current workspace.
 * Created by rmemoria on 26/1/16.
 */
public class UserWsQueryParams extends EntityQueryParams {
    // available profiles
    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";

    // available order by
    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_ADMINUNIT = "admunit";
    public static final String ORDERBY_UNIT = "unit";

    /**
     * Search for the exact name
     */
    private String name;

    /**
     * Search for part of the name (using LIKE)
     */
    private String key;

    /**
     * If true, will include non-active units
     */
    private boolean includeDisabled;

    /**
     * Search for users inside an administrative unit
     */
    private UUID adminUnitId;

    /**
     * Search for users from a given unit
     */
    private UUID unitId;

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

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }
}
