package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.commons.models.FieldManager;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;

/**
 * Created by rmemoria on 1/7/16.
 */
public class FieldTypeResolver implements TypeIdResolver {

    private JavaType initType;

    @Override
    public void init(JavaType javaType) {
        System.out.println(javaType.toString());
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
        FieldHandler handler = FieldManager.instance().get(s);
        Class<? extends Field> fieldClass = handler.getFieldClass();
        return TypeFactory.defaultInstance().constructSpecializedType(initType, fieldClass);
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String s) {
        return typeFromId(s);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
