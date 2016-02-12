package org.msh.etbm.services.admin.userprofiles.perms;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.msh.etbm.services.permissions.Permission;
import org.msh.etbm.services.permissions.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Form request handler to return the list of system permissions in a tree structure
 *
 * Created by rmemoria on 11/2/16.
 */
@Component
public class PermissionsTreeOptions implements FormRequestHandler<List<PermissionItem>> {

    public static final String CMD_NAME = "perms-tree";

    @Autowired
    Permissions permissions;

    @Autowired
    Messages messages;

    @Override
    public String getFormCommandName() {
        return CMD_NAME;
    }

    @Override
    public List<PermissionItem> execFormRequest(FormRequest req) {
        List<PermissionItem> items = new ArrayList<>();

        for (Permission perm: permissions.getList()) {
            items.add(createItem(perm));
        }
        return items;
    }

    /**
     * Create a permission and its children in a tree structure to return as a list of options
     * @param perm The system permission
     * @return instance of {@link PermissionItem}
     */
    protected PermissionItem createItem(Permission perm) {
        PermissionItem item = new PermissionItem(perm.getId(), messages.get(perm.getMessageKey()), perm.isChangeable());

        if (perm.getChildren() != null && perm.getChildren().size() > 0) {
            List<PermissionItem> lst = new ArrayList<>();

            for (Permission child: perm.getChildren()) {
                lst.add(createItem(child));
            }

            item.setChildren(lst);
        }

        return item;
    }
}
