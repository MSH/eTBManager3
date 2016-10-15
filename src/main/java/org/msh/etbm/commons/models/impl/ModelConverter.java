package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.FieldTypeManager;
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
     * @param context
     * @return
     */
    public Map<String, Object> convert(ValidationContext context) {
        Model model = context.getModel();
        Map<String, Object> values = context.getDoc();
        Map<String, Object> newvals = new HashMap<>();

        for (Map.Entry<String, Object> entry: values.entrySet()) {
            String fname = entry.getKey();
            Field field = model.findFieldByName(fname);

            if (field != null) {
                Object val = entry.getValue();

                FieldContext fieldContext = context.createFieldContext(field);

                Object newval = convertValue(field, fieldContext, val);

                // check default values
                if (newval == null && field.getDefaultValue() != null) {
                    newval = fieldContext.evalProperty(field.getName());
                }

                newvals.put(fname, newval);
            }
        }

        checkDefaultValues(context, newvals);

        return newvals;
    }


    /**
     * Check if there are not declared fields that contains default values
     * @param validationContext
     * @param vals
     */
    protected void checkDefaultValues(ValidationContext validationContext, Map<String, Object> vals) {
        Model model = validationContext.getModel();

        for (Field field: model.getFields()) {
            if (field.getDefaultValue() != null && !vals.containsKey(field.getName())) {
                FieldContext fieldContext = validationContext.createFieldContext(field);
                vals.put(field.getName(), fieldContext.evalProperty("defaultValue"));
            }
        }
    }

    /**
     * Convert a single value according to the given field
     * @param field
     * @param value
     * @return
     */
    protected Object convertValue(Field field, FieldContext context, Object value) {
        FieldHandler handler = FieldTypeManager.instance().getHandler(field.getTypeName());

        if (handler == null) {
            throw new ModelException("Handler not found for field " + field.getClass().getName());
        }

        return handler.convert(field, context, value);
    }
}
