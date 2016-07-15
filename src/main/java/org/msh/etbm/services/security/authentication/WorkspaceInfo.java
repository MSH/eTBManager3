package org.msh.etbm.services.security.authentication;

import java.util.UUID;

/**
 * Information about a workspace that a user is assigned to
 * Created by rmemoria on 29/9/15.
 */
public class WorkspaceInfo {

    private UUID id;
    private String name;
    private String unitName;

    public WorkspaceInfo() {
        super();
    }

    public WorkspaceInfo(UUID id, String name, String unitName) {
        super();
        this.id = id;
        this.name = name;
        this.unitName = unitName;
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the name1
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name1 to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
