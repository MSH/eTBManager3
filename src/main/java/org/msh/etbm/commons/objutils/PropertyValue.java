package org.msh.etbm.commons.objutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Store a property value
 *
 * Created by rmemoria on 9/3/16.
 */
public class PropertyValue {

    private Object value;

    private List<CollectionItem> items;

    public PropertyValue(Object value) {
        this.value = value;
        discoverValue();
    }


    private void discoverValue() {
        if (!isCollection()) {
            return;
        }

        // get the reference to the list
        List lst = value.getClass().isArray() ? Arrays.asList(value) : (List) value;

        items = new ArrayList<>();
        for (Object obj: lst) {
            int hash = ObjectUtils.hash(obj);
            items.add(new CollectionItem(obj, hash));
        }
    }

    /**
     * Return the stored value
     * @return object
     */
    public Object get() {
        return value;
    }

    /**
     * If a property is a collection, search for the item in the collection by its value
     * @param value the value to search for inside the collection
     * @return the instance of {@link CollectionItem} assigned to the value, or null if none is found
     */
    public CollectionItem findItemByValue(Object value) {
        checkCollection();

        for (CollectionItem item: items) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Check if property stores a collection. If not, an exception is thrown
     */
    protected void checkCollection() {
        if (items == null) {
            throw new ObjectAccessException("Property is not a collection");
        }
    }

    /**
     * If value is an array or collection, return the list of elements
     * @return
     */
    public List<CollectionItem> getItems() {
        return items;
    }

    /**
     * Check if the value is null
     * @return true if value is null
     */
    public boolean isNull() {
        return value == null;
    }

    /**
     * Check if value is a collection or an array of elements
     * @return
     */
    public boolean isCollection() {
        return value != null && (value instanceof Collection || value.getClass().isArray());
    }
}
