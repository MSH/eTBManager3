package org.msh.etbm.commons.models;

import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.IntegerField;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.msh.etbm.commons.models.data.handlers.*;


import java.util.HashMap;
import java.util.Map;

/**
 * Maintain a list of all available field types to be used in models
 * Created by rmemoria on 1/7/16.
 */
public class FieldManager {

    // generates a singleton instance
    private static final FieldManager _instance = new FieldManager();

    private Map<String, FieldHandler> fields = new HashMap<>();

    public FieldManager() {
        registerCommonFields();
    }

    protected void registerCommonFields() {
        register(new StringFieldHandler());
        register(new IntegerFieldHandler());
        register(new BoolFieldHandler());
        register(new DateFieldHandler());
    }

    /**
     * Register a new field type
     * @param handler The field handler
     */
    public void register(FieldHandler handler) {
        String typeName = handler.getTypeName();

        if (fields.containsKey(typeName)) {
            throw new ModelException("Field type already registered: " + typeName);
        }

        fields.put(typeName, handler);
    }

    /**
     * Return the field class by its type
     * @param typeName
     * @return
     */
    public FieldHandler get(String typeName) {
        return fields.get(typeName);
    }

    public static FieldManager instance() {
        return _instance;
    }

}
