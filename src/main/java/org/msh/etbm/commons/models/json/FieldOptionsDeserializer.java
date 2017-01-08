package org.msh.etbm.commons.models.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.options.FieldListOptions;
import org.msh.etbm.commons.models.data.options.FieldOptions;
import org.msh.etbm.commons.models.data.options.FieldRangeOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 8/1/17.
 */
public class FieldOptionsDeserializer extends JsonDeserializer<FieldOptions> {

    @Override
    public FieldOptions deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node =  jsonParser.getCodec().readTree(jsonParser);

        // there are two possibilities : either it is a range containing 'from' and 'to' properties
        // or it is a list of options

        if (node.isObject()) {
            return deserializeRange(node);
        }

        if (node.isArray()) {
            return deserializeOptions(node);
        }

        throw new ModelException("Invalid options: " + node.toString());
    }

    private FieldOptions deserializeOptions(JsonNode node) {
        List<Item> lst = new ArrayList<>();

        for (JsonNode itemNode: node) {
            String id = itemNode.get("id").asText();
            String name = itemNode.get("name").asText();

            Item item = new Item(id, name);
            lst.add(item);
        }

        FieldListOptions opt = new FieldListOptions();
        opt.setList(lst);

        return opt;
    }

    private FieldOptions deserializeRange(JsonNode node) {
        Integer from = node.get("from").asInt();
        Integer to = node.get("to").asInt();

        FieldRangeOptions opt = new FieldRangeOptions();
        opt.setFrom(from);
        opt.setTo(to);

        return opt;
    }
}
