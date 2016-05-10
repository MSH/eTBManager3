package org.msh.etbm.commons.objutils;

/**
 * Store information about an item of a collection
 *
 * Created by rmemoria on 9/3/16.
 */
public class CollectionItem {

    /**
     * The item value
     */
    private Object value;

    /**
     * The item hash, for future comparison
     */
    private int hash;

    public CollectionItem(Object value, int hash) {
        this.value = value;
        this.hash = hash;
    }

    public Object getValue() {
        return value;
    }

    public int getHash() {
        return hash;
    }
}
