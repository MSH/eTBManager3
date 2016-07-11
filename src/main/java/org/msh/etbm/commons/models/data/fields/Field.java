package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.FieldOptions;
import org.msh.etbm.commons.models.data.JSExprValue;
import org.msh.etbm.commons.models.data.Validator;

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
    private String name;

    /**
     * List of validators of the field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Validator> validators;

    /**
     * Indicate if the field is required or not
     */
    private JSExprValue<Boolean> required = new JSExprValue<>(false);

    /**
     * The description label of the field
     */
    private String label;

    /**
     * The default value, if none is informed to the record
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object defaultValue;

    /**
     * Is a custom field, i.é, not part of the model but created by the user
     */
    private boolean custom;

    /**
     * List of possible values to the field
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FieldOptions options;


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

    public JSExprValue<Boolean> getRequired() {
        return required;
    }

    public void setRequired(JSExprValue<Boolean> required) {
        this.required = required;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
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
}
