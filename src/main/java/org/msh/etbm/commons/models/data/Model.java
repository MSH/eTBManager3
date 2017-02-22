package org.msh.etbm.commons.models.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by rmemoria on 1/7/16.
 */
public class Model {

    /**
     * The model unique identifier ID
     */
    @NotNull
    private String name;

    /**
     * The table to store the model record
     */
    @NotNull
    private String table;

    /**
     * The version of this model. It is used for optimistic locking and
     * for changing detection
     */
    @NotNull
    private long version;

    /**
     * The list of fields of a model
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    @NotNull
    private List<Field> fields;

    /**
     * The list of validators of the model
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    private List<Validator> validators;


    /**
     * Search for the field by its ID
     * @param id the field ID
     * @return the field with the same given ID, or null if not found
     */
    public Field findFieldById(int id) {
        if (fields == null) {
            return null;
        }

        for (Field field: fields) {
            if (field.getId() == id) {
                return field;
            }
        }

        return null;
    }

    /**
     * Search for a field by its name
     * @param name
     * @return
     */
    public Field findFieldByName(String name) {
        if (fields != null) {
            for (Field field: fields) {
                if (name.equals(field.getName())) {
                    return field;
                }
            }
        }

        return null;
    }

    /**
     * Resolve the table name. If table property is not defined, use the model name
     * @return
     */
    public String resolveTableName() {
        return table != null ? table : name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
