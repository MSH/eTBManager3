package org.msh.etbm.services.admin.userprofiles.perms;

import org.msh.etbm.commons.Item;

import java.util.List;

/**
 * Store in-memory information about a permission and its children to be returned by the {@link PermissionsTreeOptions}
 * component
 *
 * Created by rmemoria on 11/2/16.
 */
public class PermissionItem extends Item<String> {

    private boolean changeable;

    /**
     * Permission children of this permission
     */
    private List<PermissionItem> children;

    /**
     * Default no-args constructor
     */
    public PermissionItem() {
        super();
    }

    /**
     * Constructor to pass the permission data
     * @param id
     * @param name
     * @param changeable
     */
    public PermissionItem(String id, String name, boolean changeable) {
        super(id, name);
        this.changeable = changeable;
    }

    public List<PermissionItem> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionItem> children) {
        this.children = children;
    }

    public boolean isChangeable() {
        return changeable;
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }
}
