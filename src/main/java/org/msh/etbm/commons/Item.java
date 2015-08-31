package org.msh.etbm.commons;

/**
 * General purpose class to store information about an item (from list of options, entity collections, etc.)
 *
 * Created by rmemoria on 30/8/15.
 */
public class Item<E> {
    private E id;
    private String text;

    /**
     * Default constructor
     */
    public Item() {
        super();
    }

    /**
     * Constructor passing ID and text
     * @param id
     * @param text
     */
    public Item(E id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
