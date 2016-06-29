package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.services.admin.userprofiles.perms.PermissionItem;
import org.msh.etbm.services.admin.userprofiles.perms.PermissionsTreeOptions;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by rmemoria on 11/2/16.
 */
public class PermissionTreeOptionsTest extends AuthenticatedTest {

    @Autowired
    PermissionsTreeOptions permissionsTreeOptions;

    @Test
    public void checkOptions() {
        assertNotNull(permissionsTreeOptions.getFormCommandName());

        FormRequest req = new FormRequest(PermissionsTreeOptions.CMD_NAME, "any");

        List<PermissionItem> lst = permissionsTreeOptions.execFormRequest(req);

        assertNotNull(lst);

        for (PermissionItem item : lst) {
            assertItem(item);
            assertNotNull(item.getChildren());

            for (PermissionItem child : item.getChildren()) {
                assertItem(child);
            }
        }
    }

    protected void assertItem(PermissionItem item) {
        assertNotNull(item.getId());
        assertNotNull(item.getName());
    }
}
