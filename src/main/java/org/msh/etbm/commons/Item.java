package org.msh.etbm.commons;

/**
 * General purpose class to store information about an item (from list of options, entity collections, etc.)
 *
 * Created by rmemoria on 30/8/15.
 */
public class Item<E> {
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
     * @param id the id of the item
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
}
