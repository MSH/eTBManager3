package org.msh.etbm.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

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
}
