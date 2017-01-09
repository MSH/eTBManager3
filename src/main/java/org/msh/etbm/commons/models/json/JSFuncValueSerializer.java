package org.msh.etbm.commons.models.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.msh.etbm.commons.models.data.JSFuncValue;

import java.io.IOException;

/**
 * Created by rmemoria on 8/1/17.
 */
public class JSFuncValueSerializer extends JsonSerializer<JSFuncValue> {

    @Override
    public void serialize(JSFuncValue jsFuncValue, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (jsFuncValue.isExpressionPresent()) {
            jgen.writeStartObject();
            jgen.writeStringField("function", jsFuncValue.getFunction());
            jgen.writeEndObject();
            return;
        }

        if (jsFuncValue.isValuePresent()) {
            jgen.writeObject(jsFuncValue.getValue());
            return;
        }

        jgen.writeNull();
    }
}
