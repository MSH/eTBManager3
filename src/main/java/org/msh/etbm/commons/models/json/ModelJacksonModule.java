package org.msh.etbm.commons.models.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.models.data.JSFunction;
import org.msh.etbm.commons.models.data.options.FieldOptions;

/**
 * Simple module to include all JSON serializers and deserializers of the models
 *
 * Created by rmemoria on 8/1/17.
 */
public class ModelJacksonModule extends SimpleModule {

    public ModelJacksonModule() {
        super();

        addSerializer(FieldOptions.class, new FieldOptionsSerializer());
        addSerializer(JSFunction.class, new JSFunctionSerializer());
        addSerializer(JSFuncValue.class, new JSFuncValueSerializer());

        addDeserializer(FieldOptions.class, new FieldOptionsDeserializer());
        addDeserializer(JSFuncValue.class, new JSFuncValueDeserializer());
        addDeserializer(JSFunction.class, new JSFunctionDeserializer());
    }
}
