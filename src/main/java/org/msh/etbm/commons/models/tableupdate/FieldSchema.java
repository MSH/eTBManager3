package org.msh.etbm.commons.models.tableupdate;

import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.TableColumn;

/**
 * Store information about a field of a model and its table structure. Used in operations
 * for updating the table structure of a model
 *
 * Created by rmemoria on 3/1/17.
 */
public class FieldSchema {

    /**
     * The field assigned to the schema
     */
    private Field field;

    /**
     * The new field name, in case it was changed
     */
    private String newName;

    /**
     * The table schema
     */
    private TableColumn schema;


    public FieldSchema(Field field, String newName, TableColumn schema) {
        this.field = field;
        this.newName = newName;
        this.schema = schema;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public TableColumn getSchema() {
        return schema;
    }

    public void setSchema(TableColumn schema) {
        this.schema = schema;
    }

}
