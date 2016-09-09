package org.msh.etbm.commons.indicators.filters;

/**
 * Filter value iterator that handles integer values. The integer value
 * is automatically converted from the iterator string value
 * Created by rmemoria on 28/5/15.
 */
public abstract class ValueIteratorInt implements ValueHandler.ValueIterator {
    @Override
    public String iterate(String value, int index) {
        if (value == null) {
            return iterateInt(null, index);
        } else {
            return iterateInt(Integer.parseInt(value), index);
        }
    }

    /**
     * Iterate by integer value converted from the string value
     * @param value
     * @return
     */
    public abstract String iterateInt(Integer value, int index);
}
