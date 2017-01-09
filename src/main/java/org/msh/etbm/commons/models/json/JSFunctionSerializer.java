package org.msh.etbm.commons.models.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.msh.etbm.commons.models.data.JSFunction;

import java.io.IOException;

/**
 * Custom serializer to convert a {@link JSFunction} instance to a JSON data
 *
 * Created by rmemoria on 8/1/17.
 */
public class JSFunctionSerializer extends JsonSerializer<JSFunction> {

    @Override
    public void serialize(JSFunction jsFunction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (jsFunction.getExpression() != null) {
            jsonGenerator.writeString(jsFunction.getExpression());
        } else {
            jsonGenerator.writeNull();
        }
    }
}
