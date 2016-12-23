package org.msh.etbm.commons.indicators.keys;


import org.msh.etbm.commons.indicators.IndicatorException;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Store multiple keys
 * Created by rmemoria on 12/12/16.
 */
public class MultipleKeys extends Key {

    private List<Key> keys = new ArrayList<Key>();


    protected MultipleKeys() {
        super(null, null);
    }

    /**
     * Add a key to the list of keys
     * @param key
     */
    public void addKey(Key key) {
        keys.add(key);
    }

    /**
     * Return the list of keys
     * @return
     */
    public List<Key> getKeys() {
        return Collections.unmodifiableList(keys);
    }

    @Override
    public Object getValue() {
        throwError();
        return null;
    }

    @Override
    public Object getGroup() {
        throwError();
        return null;
    }

    @Override
    public void setVariable(Variable variable) {
        super.setVariable(variable);
        for (Key key: keys) {
            key.setVariable(variable);
        }
    }

    private void throwError() {
        throw new IndicatorException("Invalid method when storing multiple keys");
    }
}
