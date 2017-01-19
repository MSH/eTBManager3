package org.msh.etbm.commons.dbcache;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 16/1/17.
 */
public class JsonArgumentsHandler {

    public static String generateJson(ObjectMapper mapper, Object[] args) {
        StringWriter writer = new StringWriter();
        JsonGenerator generator;
        try {
            generator = mapper.getFactory().createGenerator(writer);

            if (args == null) {
                return null;
            }

            generator.writeStartArray();
            for (Object arg: args) {
                writeArgument(generator, arg);
            }
            generator.writeEndArray();

            generator.close();

            return writer.toString();
        } catch (IOException e) {
            throw new DbCacheException(e);
        }
    }

    private static void writeArgument(JsonGenerator gen, Object obj) throws IOException {
        gen.writeStartObject();
        if (obj == null) {
            gen.writeStringField("type", "null");
        } else {
            gen.writeStringField("type", obj.getClass().getCanonicalName());
            gen.writeFieldName("data");
            JsonUtils.writeAny(gen, obj);
        }
        gen.writeEndObject();
    }

    /**
     * Parse a list of arguments that were serialized before with the
     * {@link #generateJson(ObjectMapper, Object[])} method
     * @param mapper the instance of ObjectMapper
     * @param json the JSON content in string format
     * @return the list of arguments in an object array
     */
    public static Object[] parseJson(ObjectMapper mapper, String json) {
        System.out.println("JSON = " + json);
        try {
            JsonParser parser = mapper.getFactory().createParser(json);
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new DbCacheException("Expected array begin");
            }

            List args = new ArrayList();

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                Object arg = parseArgument(parser);
                args.add(arg);
            }

            return args.toArray();
        } catch (IOException e) {
            throw new DbCacheException(e);
        }
    }

    private static Object parseArgument(JsonParser parser) throws IOException {
        JsonToken token = parser.nextToken();
        if (!"type".equals(parser.getCurrentName())) {
            throw new DbCacheException("Expected 'id field in argument JSON data");
        }

        parser.nextToken();
        String clazzName = parser.getValueAsString();
        if ("null".equals(clazzName)) {
            return null;
        }

        Class clazz = ObjectUtils.forClass(clazzName);

        parser.nextToken();
        if (!"data".equals(parser.getCurrentName())) {
            throw new DbCacheException("Expected property 'data' field in argument JSON data");
        }

        parser.nextToken();
        Object res = JsonUtils.readValue(parser, clazz);

        if (parser.nextToken() != JsonToken.END_OBJECT) {
            throw new DbCacheException("Expected end of object, found " + parser.getCurrentName());
        }

        return res;
    }
}
