package org.msh.etbm.commons.models.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.JSFunction;

import java.io.IOException;

/**
 * Created by rmemoria on 8/1/17.
 */
public class JSFunctionDeserializer extends JsonDeserializer<JSFunction> {

    @Override
    public JSFunction deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.isNull()) {
            return null;
        }

        if (node.isTextual()) {
            return new JSFunction(node.asText());
        }

        throw new ModelException("Invalid JSON data structure: " + node.toString());
    }
}
