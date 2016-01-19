package org.msh.etbm.commons.forms;

import org.msh.etbm.commons.forms.handlers.AdminUnitTypeHandler;
import org.msh.etbm.commons.forms.handlers.UnitTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for registering and providing type handlers
 *
 * Created by rmemoria on 17/1/16.
 */
@Component
public class TypesManagerService {

    @Autowired
    AdminUnitTypeHandler adminUnitTypeHandler;

    @Autowired
    UnitTypeHandler unitTypeHandler;


    private Map<String, TypeHandler> types = new HashMap<>();


    /**
     * Register all supported type handlers
     */
    @PostConstruct
    void initialize() {
        register(adminUnitTypeHandler);
        register(unitTypeHandler);
    }

    /**
     * Register a new type handler
     * @param handler the instance of the type handler to register
     */
    void register(TypeHandler handler) {
        String name = handler.getTypeName();

        if (types.containsKey(name)) {
            throw new RuntimeException("Name is already registered: " + name);
        }
        types.put(name, handler);
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
