package org.msh.etbm.commons.models.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.FieldType;
import org.msh.etbm.commons.models.data.fields.FieldTypeResolver;
import org.msh.etbm.commons.models.data.options.FieldOptions;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by rmemoria on 1/7/16.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(FieldTypeResolver.class)
public abstract class Field {

    /**
     * Field name
     */
    @NotNull
    private String name;

    /**
     * The field ID. All fields must have an ID. Is by the ID that the Model engine
     * checks if the field name has changed, on a model update
     */
    @NotNull
    private int id;

    /**
     * List of validators of the field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    private List<Validator> validators;

    /**
     * Indicate if the field is required or not
     */
    private JSFuncValue<Boolean> required;

    /**
     * The description label of the field
     */
    @NotNull
    private String label;

    /**
     * The default value, if none is informed to the record
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JSFuncValue defaultValue;

    /**
     * Is a custom field, i.é, not part of the model but created by the user
     */
    private boolean custom;

    /**
     * If true, this value must be unique in the column
     */
    @JsonIgnore
    private boolean unique;

    /**
     * List of possible values to the field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FieldOptions options;

    /**
     * A simple java script expression to validate the field. The return of the expression is the
     * message to be displayed, or return null or unassigned to indicate it was validated
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JSFunction validate;


    public Field() {
        super();
    }

    public Field(String name) {
        super();
        this.name = name;
    }

    /**
     * Return the type name uded in the class
     * @return
     */
    @JsonIgnore
    public String getTypeName() {
        FieldType ftype = getClass().getAnnotation(FieldType.class);
        if (ftype == null) {
            throw new ModelException("Annotation " + FieldType.class.getName() + " not found in class " + getClass().getName());
        }

        return ftype.value();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

    public JSFuncValue<Boolean> getRequired() {
        return required;
    }

    public void setRequired(JSFuncValue<Boolean> required) {
        this.required = required;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JSFuncValue getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(JSFuncValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public FieldOptions getOptions() {
        return options;
    }

    public void setOptions(FieldOptions options) {
        this.options = options;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public JSFunction getValidate() {
        return validate;
    }

    public void setValidate(JSFunction validate) {
        this.validate = validate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
