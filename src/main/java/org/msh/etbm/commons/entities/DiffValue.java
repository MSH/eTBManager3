package org.msh.etbm.commons.entities;

/**
 * Store previous and new value and check if they are different
 * Created by rmemoria on 17/10/15.
 */
public class DiffValue {
    private Object prevValue;
    private Object newValue;

    public DiffValue() {
        super();
    }

    public DiffValue(Object prevValue, Object newValue) {
        super();
        this.prevValue = prevValue;
        this.newValue = newValue;
    }

    /**
     * Return true if values are different
     * @return
     */
    public boolean isDifferent() {
        if (prevValue == newValue) {
            return false;
        }

        if (prevValue == null || newValue == null) {
            return true;
        }

        return !prevValue.equals(newValue);
    }

    public Object getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(Object prevValue) {
        this.prevValue = prevValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }
}
