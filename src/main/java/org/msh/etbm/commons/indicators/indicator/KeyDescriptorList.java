package org.msh.etbm.commons.indicators.indicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rmemoria on 3/10/16.
 */
public class KeyDescriptorList {

    private List<KeyDescriptor> items = new ArrayList<>();

    public List<KeyDescriptor> getGroups() {
        return Collections.unmodifiableList(items);
    }

    public KeyDescriptor add(Object id, String title) {
        // search first
        for (KeyDescriptor kd: items) {
            if (kd.getId().equals(id)) {
                kd.setName(title);
                return kd;
            }
        }

        KeyDescriptor item = new KeyDescriptor(this, null, id, title);
        items.add(item);
        return item;
    }

    /**
     * Search for a key descriptor by its key
     * @param key the key descriptor
     * @return the instance of {@link KeyDescriptor} assigned to the key
     */
    public KeyDescriptor findKey(Object[] key) {
        List<KeyDescriptor> lst = items;
        int index = 0;

        while (index < key.length) {
            KeyDescriptor aux = null;
            for (KeyDescriptor item: lst) {
                if (item.getId().equals(key[index])) {
                    if (index == key.length - 1) {
                        return item;
                    }
                    aux = item;
                    break;
                }
            }

            // item was not found ?
            if (aux == null) {
                break;
            }

            lst = aux.getChildren();
            index++;
        }

        return null;
    }
}
