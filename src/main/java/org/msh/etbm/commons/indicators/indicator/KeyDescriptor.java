package org.msh.etbm.commons.indicators.indicator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rmemoria on 3/10/16.
 */
public class KeyDescriptor extends Item<Object> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KeyDescriptor> children;

    @JsonIgnore
    private KeyDescriptor parent;

    @JsonIgnore
    private KeyDescriptorList list;

    protected KeyDescriptor(KeyDescriptorList lst, KeyDescriptor parent, Object id, String name) {
        super(id, name);
        this.parent = parent;
        this.list = lst;
    }

    public KeyDescriptor add(Object id, String name) {
        if (children == null) {
            children = new ArrayList<>();
        } else {
            // search for key
            for (KeyDescriptor kd: children) {
                if (kd.getId().equals(id)) {
                    kd.setName(name);
                    return kd;
                }
            }
        }

        KeyDescriptor grp = new KeyDescriptor(list, this, id, name);
        children.add(grp);

        return grp;
    }

    public List<KeyDescriptor> getChildren() {
        return children != null ? Collections.unmodifiableList(children) : null;
    }

    @JsonIgnore
    public KeyDescriptor getParent() {
        return parent;
    }

    /**
     * Get the key item level, counting the number of parents
     * @return int value
     */
    @JsonIgnore
    public int getLevel() {
        int level = 0;
        KeyDescriptor item = this;
        while (item.getParent() != null) {
            item = item.getParent();
            level++;
        }
        return level;
    }

    /**
     * Return the key from its descriptor until its root
     * @return array of objects
     */
    @JsonIgnore
    public Object[] getKeyPath() {
        Object[] keys = new Object[getLevel()];

        KeyDescriptor item = this;

        int index = keys.length - 1;
        while (index >= 0) {
            keys[index] = item.getId();
            item = item.getParent();
            index--;
        }

        return keys;
    }

}
