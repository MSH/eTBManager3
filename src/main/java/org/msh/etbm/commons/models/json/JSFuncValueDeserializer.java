package org.msh.etbm.commons.models.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.JSFuncValue;

import java.io.IOException;


/**
 * Created by rmemoria on 8/1/17.
 */
public class JSFuncValueDeserializer extends JsonDeserializer<JSFuncValue> {

    @Override
    public JSFuncValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node =  jsonParser.getCodec().readTree(jsonParser);

        if (node.isObject()) {
            return deserializeObject(node);
        }


        return JSFuncValue.of(getValue(node));
    }

    private Object getValue(JsonNode node) {
        if (node.isNull()) {
            return null;
        }

        if (node.isBoolean()) {
            return node.asBoolean();
        }

        if (node.isInt()) {
            return node.asInt();
        }

        if (node.isLong()) {
            return node.asLong();
        }

        if (node.isFloat() || node.isDouble()) {
            return node.asDouble();
        }

        throw new ModelException("Type not supported: " + node.toString());
    }

    private JSFuncValue deserializeObject(JsonNode node) {
        String func = node.get("function").asText();

        return func != null ? JSFuncValue.function(func) : JSFuncValue.of(getValue(node));
    }
}
