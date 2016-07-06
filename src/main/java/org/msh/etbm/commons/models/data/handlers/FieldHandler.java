package org.msh.etbm.commons.models.data.handlers;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.FieldType;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.validation.Errors;

import javax.script.SimpleBindings;
import java.util.Map;

/**
 * Created by rmemoria on 1/7/16.
 */
public abstract class FieldHandler<E extends Field> {

    public Class<E> getFieldClass() {
        return ObjectUtils.getGenericType(this.getClass(), 0);
    }


    /**
     * Called during conversion phase, and convert (if necessary) the value to a proper value
     * @param field Instance of {@link Field} representing the field
     * @param value The value to be converted
     * @return the new value
     */
    protected abstract Object convertValue(E field, Object value);

    protected abstract void validateValue(E field, FieldContext context, Object value);

    public String getTypeName() {
        Class clazz = getFieldClass();
        FieldType ftype = (FieldType)clazz.getAnnotation(FieldType.class);
        if (ftype == null) {
            throw new ModelException("Annotation " + FieldType.class.getName() + " not found in class " + clazz.getName());
        }
        
        return ftype.value();
    }

    /**
     * Make any conversion (if necessary) to the field value, or simply return it if no conversion is necessary
     * @param field the instance of {@link Field} representing the field
     * @param value the field value
     * @return the new field value
     */
    public Object convert(E field, Object value) {
        // check if value is null and there is a default value
        if (value == null && field.getDefaultValue() != null) {
            return field.getDefaultValue();
        }

        return convertValue(field, value);
    }


    public void validate(E field, FieldContext fieldContext, Object value) {
        // is field required ?
        boolean required = fieldContext.evalBoolProperty("required");

        if (value == null && !required) {
            return;
        }

        validateValue(field, fieldContext, value);
    }

    protected void raiseConvertionError() {
        throw new InvalidArgumentException("Invalid type");
    }
}
