package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.FieldType;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.validation.Errors;

/**
 * Created by rmemoria on 1/7/16.
 */
public abstract class FieldHandler<E extends Field> {

    public Class<E> getFieldClass() {
        return ObjectUtils.getGenericType(this.getClass(), 0);
    }

    public String getTypeName() {
        Class clazz = getFieldClass();
        FieldType ftype = (FieldType)clazz.getAnnotation(FieldType.class);
        if (ftype == null) {
            throw new ModelException("Annotation " + FieldType.class.getName() + " not found in class " + clazz.getName());
        }
        
        return ftype.value();
    }

    public Object convert(E field, Object value) {
        // check if value is null and there is a default value
        if (value == null && field.getDefaultValue() != null) {
            return field.getDefaultValue();
        }

        return convertValue(field, value);
    }

    protected abstract Object convertValue(E field, Object value);

    protected void raiseConvertionError() {
        throw new InvalidArgumentException("Invalid type");
    }
}
