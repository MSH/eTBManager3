package org.msh.etbm.commons.models.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.options.FieldListOptions;
import org.msh.etbm.commons.models.data.options.FieldOptions;
import org.msh.etbm.commons.models.data.options.FieldRangeOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by rmemoria on 7/1/17.
 */
public class FieldOptionsSerializer extends JsonSerializer<FieldOptions> {

    @Override
    public void serialize(FieldOptions fieldOptions, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (fieldOptions instanceof FieldListOptions) {
            serializeFieldListOptions((FieldListOptions)fieldOptions, jsonGenerator);
            return;
        }

        if (fieldOptions instanceof FieldRangeOptions) {
            serializeFieldRangeOptions((FieldRangeOptions)fieldOptions, jsonGenerator);
            return;
        }

        throw new ModelException("Field options class not supported: " + fieldOptions.getClass().toString());
    }

    private void serializeFieldRangeOptions(FieldRangeOptions fieldOptions, JsonGenerator jgen)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("from", fieldOptions.getFrom());
        jgen.writeNumberField("to", fieldOptions.getTo());
        jgen.writeEndObject();
    }

    private void serializeFieldListOptions(FieldListOptions fieldOptions, JsonGenerator jgen)
            throws IOException {
        List<Item> lst = fieldOptions.getList();

        jgen.writeStartArray();
        if (lst != null) {
            for (Item item: lst) {
                jgen.writeStartObject();
                jgen.writeObjectField("id", item.getId());
                jgen.writeStringField("name", item.getName());
                jgen.writeEndObject();
            }
        }
        jgen.writeEndArray();
    }

}
