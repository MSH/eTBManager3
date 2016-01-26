package org.msh.etbm.services.admin.userprofiles;

import java.util.List;
import java.util.Optional;

/**
 * Form data containing a user profile, used to initialize a new form or update information about a profile
 * Created by rmemoria on 26/1/16.
 */
public class UserProfileFormData {
    private Optional<String> name;
    private Optional<String> customId;
    private List<UserPermissionData> permissions;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }

    public List<UserPermissionData> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<UserPermissionData> permissions) {
        this.permissions = permissions;
    }
}
