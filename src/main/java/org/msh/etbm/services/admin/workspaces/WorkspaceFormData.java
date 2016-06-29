package org.msh.etbm.services.admin.workspaces;

import java.util.Optional;

/**
 * Data to be used in a form editor in the client side
 * <p>
 * Created by rmemoria on 9/4/16.
 */
public class WorkspaceFormData {

    private Optional<String> name;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }
}
