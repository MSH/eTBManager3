package org.msh.etbm.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.UUID;

/**
 * Class with utilities to help working with JSON files, data and serialization
 * <p>
 * Created by rmemoria on 2/9/15.
 */
public class JsonUtils {

    private final static Logger log = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Avoid instantiation of this class
     */
    private JsonUtils() {
        super();
    }

    /**
     * Read a json file from the application resources and convert it to a given Java type
     *
     * @param resource the full json file name of the resource in the system
     * @param type     the type to parse the file into
     * @param <T>
     * @return instance of the type T
     */
    public static <T> T parseResource(String resource, Class<T> type) {
        ClassPathResource res = new ClassPathResource(resource);

        try {
            InputStream in = res.getInputStream();
            return parse(in, type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JsonParserException(e);
        }
    }

    /**
     * Read a json file as an array from the application resources
     * @param resource
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T[] parseArrayResource(String resource, Class<T> type) {
        ClassPathResource res = new ClassPathResource(resource);

        try {
            InputStream in = res.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, TypeFactory.defaultInstance().constructArrayType(type));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JsonParserException(e);
        }
    }

    public static <T> T parse(InputStream in, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new JsonParserException(e);
        }
    }

    public static <T> T parse(InputStream in, JavaType type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new JsonParserException(e);
        }
    }

    /**
     * Convert a json String to a given Java type
     *
     * @param jsonString the json string
     * @param type       the type to parse the file into
     * @param <T>
     * @return instance of the type T
     */
    public static <T> T parseString(String jsonString, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JsonParserException(e);
        }

    }

    /**
     * Convert an object to JSON string representation
     *
     * @param obj object to serialize
     * @return JSON in string format
     */
    public static String objectToJSONString(Object obj, boolean pretty) {
        ObjectMapper mapper = new ObjectMapper();

        if (pretty) {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new JsonParserException(e);
        }
    }


    /**
     * Write in a generator a json value of any type. It tests if is a supported primitive and
     * use the proper serialization. If not a primitive, then serialize to an object value
     * @param generator the instance of {@link JsonGenerator} from the Jackson library
     * @param val the value to be serialized
     * @throws IOException
     */
    public static void writeAny(JsonGenerator generator, Object val) throws IOException {
        if (val == null) {
            generator.writeNull();
            return;
        }

        if (val instanceof String) {
            generator.writeString((String)val);
            return;
        }

        if (val instanceof Integer) {
            generator.writeNumber((Integer)val);
            return;
        }

        if (val instanceof Double) {
            generator.writeNumber((Double)val);
            return;
        }

        if (val instanceof Float) {
            generator.writeNumber((Float)val);
            return;
        }

        if (val instanceof BigInteger) {
            generator.writeNumber((BigInteger)val);
            return;
        }

        if (val instanceof Long) {
            generator.writeNumber((Long)val);
            return;
        }

        if (val instanceof Short) {
            generator.writeNumber((Short)val);
            return;
        }

        if (val instanceof Boolean) {
            generator.writeBoolean((Boolean)val);
            return;
        }

        if (val instanceof UUID) {
            byte[] data = ObjectUtils.uuidAsBytes((UUID)val);
            generator.writeBinary(data);
            return;
        }

        if (val instanceof byte[]) {
            generator.writeBinary((byte[])val);
            return;
        }

        generator.writeObject(val);
    }


    public static <E> E readValue(JsonParser parser, Class<E> clazz) throws IOException {
        if (String.class.isAssignableFrom(clazz)) {
            return (E)parser.nextValue().asString();
        }

        if (Boolean.class.isAssignableFrom(clazz)) {
            return (E)parser.nextBooleanValue();
        }


        if (Integer.class.isAssignableFrom(clazz)) {
            return (E)(Integer)parser.nextIntValue(0);
        }

        if (Long.class.isAssignableFrom(clazz)) {
            return (E)(Long)parser.nextLongValue(0L);
        }

        if (Float.class.isAssignableFrom(clazz)) {
            return (E)(Float)parser.getFloatValue();
        }

        if (Double.class.isAssignableFrom(clazz)) {
            return (E)(Double)parser.getDoubleValue();
        }

        if (UUID.class.isAssignableFrom(clazz)) {
            byte[] data = parser.getBinaryValue();
            return (E)ObjectUtils.bytesToUUID(data);
        }

        if (byte[].class.isAssignableFrom(clazz)) {
            return (E)parser.getBinaryValue();
        }

        if (BigInteger.class.isAssignableFrom(clazz)) {
            return (E)parser.getBigIntegerValue();
        }

        if (Short.class.isAssignableFrom(clazz)) {
            return (E)(Short)parser.getShortValue();
        }

        Iterator<E> it = parser.readValuesAs(clazz);

        E res = it.next();
        return res;
    }
}
