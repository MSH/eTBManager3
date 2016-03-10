package org.msh.etbm.commons.objutils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Store previous and new value and check if they are different
 * Created by rmemoria on 17/10/15.
 */
public class DiffValue {

    private Object prevValue;

    private Object newValue;

    private List addedItems;
    private List removedItems;
    private List changedItems;

    /**
     * Constructor for collections
     * @param addedItems the added items
     * @param removedItems the removed items
     * @param changedItems the changed items
     */
    public DiffValue(List addedItems, List removedItems, List changedItems) {
        this.addedItems = addedItems;
        this.removedItems = removedItems;
        this.changedItems = changedItems;

        if (addedItems == null && removedItems == null && changedItems == null) {
            throw new ObjectAccessException("When declaring a collection diff, at least addedItems, " +
                    "removedItems or changedItems must be informed");
        }
    }

    public DiffValue(Object prevValue, Object newValue) {
        super();
        this.prevValue = prevValue;
        this.newValue = newValue;

        if (prevValue == newValue) {
            throw new ObjectAccessException("Previous and new value cannot be the same");
        }
    }


    /**
     * Return true if the difference values are from a collection
     * @return
     */
    public boolean isCollection() {
        return addedItems != null || removedItems != null || changedItems != null;
    }

    public Object getPrevValue() {
        return prevValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public List getAddedItems() {
        return addedItems;
    }

    public List getRemovedItems() {
        return removedItems;
    }

    public List getChangedItems() {
        return changedItems;
    }
}
