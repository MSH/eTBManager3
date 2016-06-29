package org.msh.etbm.commons;

import java.util.UUID;

/**
 * Extension of the item class to cover items that are synchronizable
 * <p>
 * Created by rmemoria on 13/11/15.
 */
public class SynchronizableItem extends Item<UUID> {
    public SynchronizableItem() {
        super();
    }

    public SynchronizableItem(UUID id, String name) {
        super(id, name);
    }
}
