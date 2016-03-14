package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.commons.SynchronizableItem;

import java.util.UUID;

/**
 * Item information about an user of a workspace
 * Created by rmemoria on 14/3/16.
 */
public class UserWsItemData extends SynchronizableItem {

    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
