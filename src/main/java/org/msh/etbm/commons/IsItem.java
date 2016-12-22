package org.msh.etbm.commons;

/**
 * Interface to indicate that an object supports the usage of its class as an item,
 * i.e, it contains an ID and a displayable name
 *
 * Created by rmemoria on 20/12/16.
 */
public interface IsItem<E> {

    /**
     * Return the ID of the item. Usually the ID is unique among all other items
     * @return The String ID
     */
    E getId();

    /**
     * Display the displayable name of the item
     * @return String containing the name
     */
    String getName();
}
