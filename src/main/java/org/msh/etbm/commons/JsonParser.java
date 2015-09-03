package org.msh.etbm.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.services.init.impl.NewWorkspaceTemplate;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * Created by rmemoria on 2/9/15.
 */
public class JsonParser {

    /**
     * Read a json file from the application resources and convert it to a given Java type
     * @param resource the full json file name of the resource in the system
     * @param type the type to parse the file into
     * @param <T>
     * @return instance of the type T
     */
    public static <T> T parseResource(String resource, Class<T> type) {
        ClassPathResource res = new ClassPathResource(resource);
        try {
            InputStream in = res.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, type);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
