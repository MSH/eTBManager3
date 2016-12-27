package org.msh.etbm.commons.models;

import org.msh.etbm.commons.models.data.fields.*;
import org.msh.etbm.commons.models.data.handlers.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintain a list of all available field types to be used in models
 * Created by rmemoria on 1/7/16.
 */
public class FieldTypeManager {

    // generates a singleton instance
    private static final FieldTypeManager _instance = new FieldTypeManager();

    private Map<String, FieldHandler> fields = new HashMap<>();

    public FieldTypeManager() {
        registerCommonFields();
    }

    /**
     * Register common field declared in the fields package
     */
    protected void registerCommonFields() {
        register(new StringFieldHandler());
        register(new IntegerFieldHandler());
        register(new BoolFieldHandler());
        register(new DateFieldHandler());
        register(new EnumFieldHandler());

        // generic foreign key handler
        register(new ForeignKeyFieldHandler<>(ForeignKeyField.class));

        register(new ForeignKeyFieldHandler<FKCaseField>(FKCaseField.class));
        register(new ForeignKeyFieldHandler<FKSubstanceField>(FKSubstanceField.class));
        register(new ForeignKeyFieldHandler<FKRegimenField>(FKRegimenField.class));
        register(new ForeignKeyFieldHandler<FKAdminUnitField>(FKAdminUnitField.class));
        register(new FKUnitFieldHandler());

        register(new AddressFieldHandler());
        register(new PersonNameFieldHandler());

        register(new MonthYearFieldHandler());
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
     * Return the field handler by its type name
     * @param typeName
     * @return
     */
    public FieldHandler getHandler(String typeName) {
        return fields.get(typeName);
    }

    /**
     * Return the field handler by its field class
     * @param clazz the class of {@link Field} representing the field
     * @return
     */
    public FieldHandler getHandler(Class<? extends Field> clazz) {
        for (Map.Entry<String, FieldHandler> entry: fields.entrySet()) {
            if (entry.getValue().getFieldClass() == clazz) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static FieldTypeManager instance() {
        return _instance;
    }

}
