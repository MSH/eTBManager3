package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.FieldType;
import org.springframework.validation.Errors;

/**
 * Created by rmemoria on 1/7/16.
 */
public abstract class FieldHandler {

    public Class<? extends Field> getFieldClass() {
        return Field.class;
    }

    public String getTypeName() {
        Class clazz = getFieldClass();
        FieldType ftype = (FieldType)clazz.getAnnotation(FieldType.class);
        if (ftype == null) {
            throw new ModelException("Annotation " + FieldType.class.getName() + " not found in class " + clazz.getName());
        }
        
        return ftype.value();
    }

    public abstract Object convert(Object value);

    public abstract void validate(Object value, Errors errors);
}
