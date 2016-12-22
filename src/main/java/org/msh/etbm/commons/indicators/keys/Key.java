package org.msh.etbm.commons.indicators.keys;


import org.msh.etbm.commons.indicators.variables.Variable;

/**
 * Key object to store a unique value used in indicator tables
 *
 * Created by rmemoria on 12/12/16.
 */
public class Key implements Comparable<Key> {

    private Variable variable;
    private int iteration;
    private Object group;
    private Object value;

    protected Key(Object group, Object value) {
        this.group = group;
        this.value = value;
    }

    /**
     * Create a new key with no group
     * @param key the key value
     * @return
     */
    public static Key of(Object key) {
        return new Key(null, key);
    }

    /**
     * Return a null key
     */
    public static Key asNull() {
        return Key.of(null);
    }

    /**
     * Create an object that stores several keys
     * @param args
     * @return
     */
    public static MultipleKeys asMultiple(Object... args) {
        return asMultipleArray(args);
    }

    /**
     * Create an object that stores several keys from an array
     * @param args
     * @return
     */
    public static MultipleKeys asMultipleArray(Object[] args) {
        MultipleKeys keys = new MultipleKeys();

        for (Object arg: args) {
            Key key = arg instanceof Key ? (Key)arg : Key.of(arg);
            keys.addKey(key);
        }

        return keys;
    }

    /**
     * Create a new key with a group definition
     * @param group the key group
     * @param key the key value
     * @return
     */
    public static Key of(Object group, Object key) {
        return new Key(group, key);
    }

    /**
     * Compare this key to another key
     * @param key
     * @return
     */
    @Override
    public int compareTo(Key key) {
        if (this.isNull() && key.isNull()) {
            return 0;
        }

        // groups are different ?
        if (group != key.getGroup()) {
            if (group == null) {
                return -1;
            }

            if (key.getGroup() == null) {
                return 1;
            }

            if (group instanceof Comparable) {
                int res = ((Comparable) group).compareTo(key.getGroup());
                if (res != 0) {
                    return res;
                }
            }
            // if there is no way to compare, consider them equals
        }

        if (value == key.getValue()) {
            return 0;
        }

        if (value == null) {
            return -1;
        }

        if (key.getValue() == null) {
            return 1;
        }

        if (value instanceof Comparable) {
            return ((Comparable) value).compareTo(key.getValue());
        }

        return 0;
    }

    /**
     * Return true if key is null
     * @return
     */
    public boolean isNull() {
        return group == null && value == null;
    }

    public Object getValue() {
        return value;
    }

    public Object getGroup() {
        return group;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Key key = (Key) o;

        if (!variable.equals(key.variable)) {
            return false;
        }
        if (iteration != key.iteration) {
            return false;
        }
        if (group != null ? !group.equals(key.group) : key.group != null) {
            return false;
        }
        return value != null ? value.equals(key.value) : key.value == null;
    }

    @Override
    public int hashCode() {
        int result = variable.hashCode();
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }
}
