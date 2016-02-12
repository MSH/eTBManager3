package org.msh.etbm.services.permissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Declare a permission to be used in authorization throughout the system
 *
 * Created by rmemoria on 30/12/15.
 */
public class Permission {

    private String id;
    private List<Permission> children;
    private Permission parent;
    private boolean changeable;
    private String messageKey;

    /**
     * Constructor with no argument, used when creating the root element
     */
    protected Permission() {
        super();
    }

    /**
     * Constructor of a new permission
     * @param parent the parent permission
     * @param id the id of the permission. A name easily assigned to this item
     * @param changeable
     */
    protected Permission(Permission parent, String id, String messageKey, boolean changeable) {
        this.parent = parent;
        this.id = id;
        this.messageKey = messageKey;
        this.changeable = changeable;
    }

    /**
     * Find a permission with the given ID. It will check if the given ID is the permission ID.
     * If so, returns the own object. If not, it will check if the ID is the changeable permission
     * (if available) and if not, it will check in the children recursively
     * @param id the permission ID to search for
     * @return the permission with the given ID, or null if it was not found
     */
    public Permission find(String id) {
        if (id.equals(this.id)) {
            return this;
        }

        if (children != null) {
            for (Permission perm: children) {
                Permission res = perm.find(id);
                if (res != null) {
                    return res;
                }
            }
        }

        return null;
    }

    /**
     * Add a new child permission for this permission
     * @param id the ID of the permission
     * @return instance of the Permission class
     */
    public Permission add(String id, String messageKey) {
        addChild(id, messageKey, false);
        return this;
    }

    public void addPermission(Permission perm) {
        perm.parent = this;
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(perm);
    }

    /**
     * Add a new changeable child permission
     * @param id is the ID of the permission
     * @return instance of the Permission class
     */
    public Permission addChangeable(String id, String messageKey) {
        addChild(id, messageKey, true);
        return this;
    }

    private Permission addChild(String id, String messageKey, boolean changeable) {
        Permission perm = new Permission(this, id, messageKey, changeable);

        addPermission(perm);
        return perm;
    }


    public String getId() {
        return id;
    }

    public boolean isChangeable() {
        return changeable;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public Permission getParent() {
        return parent;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
