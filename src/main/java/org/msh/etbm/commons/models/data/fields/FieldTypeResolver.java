package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;

/**
 * Created by rmemoria on 1/7/16.
 */
public class FieldTypeResolver implements TypeIdResolver {

    private JavaType initType;

    @Override
    public void init(JavaType javaType) {
        this.initType = javaType;
    }

    @Override
    public String idFromValue(Object o) {
        return ((Field)o).getTypeName();
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        return ((Field)o).getTypeName();
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(String s) {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String s) {
        FieldHandler handler = FieldTypeManager.instance().getHandler(s);
        Class<? extends Field> fieldClass = handler.getFieldClass();
        return TypeFactory.defaultInstance().constructSpecializedType(initType, fieldClass);
    }

    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
