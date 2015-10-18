package org.msh.etbm.commons.forms;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmemoria on 15/10/15.
 */
public class FormValues<E> {

    private Map<String, Object> values = new HashMap<>();

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
