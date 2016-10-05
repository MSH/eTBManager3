package org.msh.etbm.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * General purpose class to store information about an item (from list of options, entity collections, etc.)
 * <p>
 * Created by rmemoria on 30/8/15.
 */
public class Item<E> implements Displayable {
    private E id;
    private String name;

    /**
     * Default constructor
     */
    public Item() {
        super();
    }

    /**
     * Constructor passing ID and text
     *
     * @param id   the id of the item
     * @param name the display name
     */
    public Item(E id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonIgnore
    public String getDisplayString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Item<?> item = (Item<?>) o;

        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
