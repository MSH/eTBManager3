package org.msh.etbm.services.admin.admunits;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.UUID;

/**
 * Query arguments to search for administrative units
 *
 * Created by rmemoria on 21/10/15.
 */
public class AdminUnitQueryParams extends EntityQueryParams {
    /**
     * Available profiles
     */
    public static final String QUERY_PROFILE_ITEM = "item";
    public static final String QUERY_PROFILE_DEFAULT = "default";
    public static final String QUERY_PROFILE_DETAILED = "detailed";
    /**
     * The partial name to search by administrative units
     */
    private String key;

    /**
     * The name of the administrative unit
     */
    private String name;

    /**
     * The parent unit ID of the units to search for
     */
    private UUID parentId;

    /**
     * If true and parentId is null, include just the root admin units
     */
    private boolean rootUnits;

    /**
     * If true, include children units
     */
    private boolean includeChildren;

    /**
     * If true, the country structure list will be included in the result list
     */
    private boolean fetchCountryStructure;

    /**
     * The workspace ID to be used in the query. This parameter is just available inside the system,
     * and it will be ignored when parsing from JSON
     */
    @JsonIgnore
    private UUID workspaceId;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isRootUnits() {
        return rootUnits;
    }

    public void setRootUnits(boolean rootUnits) {
        this.rootUnits = rootUnits;
    }

    public boolean isIncludeChildren() {
        return includeChildren;
    }

    public void setIncludeChildren(boolean includeChildren) {
        this.includeChildren = includeChildren;
    }

    public boolean isFetchCountryStructure() {
        return fetchCountryStructure;
    }

    public void setFetchCountryStructure(boolean fetchCountryStructure) {
        this.fetchCountryStructure = fetchCountryStructure;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }
}
