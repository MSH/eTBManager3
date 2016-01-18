package org.msh.etbm.commons.forms;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for registering and providing type handlers
 *
 * Created by rmemoria on 17/1/16.
 */
@Component
public class TypesManagerService {

    private Map<String, TypeHandler> types = new HashMap<>();

    /**
     * Register a new type handler
     * @param handler the instance of the type handler to register
     */
    void register(TypeHandler handler) {
        String name = handler.getTypeName();

        if (types.containsKey(name)) {
            throw new RuntimeException("Name is already registered: " + name);
        }
    }

    /**
     * Return a type handler by its type name
     * @param name the name of the type handler
     * @return instance of the {@link TypeHandler}
     */
    TypeHandler get(String name) {
        return types.get(name);
    }
}
