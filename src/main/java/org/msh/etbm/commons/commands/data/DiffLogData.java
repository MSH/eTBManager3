package org.msh.etbm.commons.commands.data;

import org.msh.etbm.commons.objutils.DiffValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Store a list of fields and its previous and new values
 * Created by rmemoria on 17/10/15.
 */
public class DiffLogData implements CommandData {

    private Map<String, DiffValue> values = new HashMap<>();

    public Map<String, DiffValue> getValues() {
        return values;
    }

    public void setValues(Map<String, DiffValue> values) {
        this.values = values;
    }

    @Override
    public DataType getType() {
        return DataType.DIFF;
    }

    @Override
    public Object getDataToSerialize() {
        return values;
    }
}
