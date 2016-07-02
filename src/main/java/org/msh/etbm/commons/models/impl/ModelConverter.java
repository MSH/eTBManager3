package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.models.FieldManager;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for converting the field values to the correct type
 *
 * Created by rmemoria on 1/7/16.
 */
public class ModelConverter {

    /**
     * Create a new map with converted values using the model and the given values
     * @param model
     * @param values
     * @return
     */
    public Map<String, Object> convert(Model model, Map<String, Object> values) {
        Map<String, Object> newvals = new HashMap<>();

        for (Map.Entry<String, Object> entry: values.entrySet()) {
            String fname = entry.getKey();
            Field field = model.findFieldByName(fname);

            if (field != null) {
                Object val = entry.getValue();
                Object newval = convertValue(field, val);
                newvals.put(fname, newval);
            }
        }

        return newvals;
    }

    /**
     * Convert a single value according to the given field
     * @param field
     * @param value
     * @return
     */
    protected Object convertValue(Field field, Object value) {
        FieldHandler handler = FieldManager.instance().get(field.getTypeName());

        if (handler == null) {
            throw new ModelException("Handler not found for field " + field.getClass().getName());
        }

        return handler.convert(value);
    }
}
