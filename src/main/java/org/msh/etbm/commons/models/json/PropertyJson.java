package org.msh.etbm.commons.models.json;

import org.msh.etbm.commons.models.CharCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a property of a model defined in a JSON file. The property may represents
 * the root of a model, when describing a complex object (when type is object and 'properties'
 * is available
 *
 * Created by rmemoria on 18/10/15.
 */
public class PropertyJson {

    private String type;
    private boolean required;
    private String caption;
    private Map<String, String> options = new HashMap<>();
    private Map<String, PropertyJson> properties;
    /**
     * The table name representing this property (in case this is a complex object)
     */
    private String table;

    /**
     * The field name this property is assigned to, if it is a primitive type
     */
    private String fieldName;

    /**
     * Validation expression
     */
    private String validate;
    private String validateMsg;

    /**
     * Indicate if the value is unique in the workspace
     */
    private boolean unique;
    private String uniqueMessage;

    private boolean email;

    /** String properties **/
    private int maxLength;
    private int minLength;
    private String regExp;
    private CharCase charCase;

    /** Date properties **/
    private boolean future;
    private boolean past;




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public Map<String, PropertyJson> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, PropertyJson> properties) {
        this.properties = properties;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getValidateMsg() {
        return validateMsg;
    }

    public void setValidateMsg(String validateMsg) {
        this.validateMsg = validateMsg;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getUniqueMessage() {
        return uniqueMessage;
    }

    public void setUniqueMessage(String uniqueMessage) {
        this.uniqueMessage = uniqueMessage;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    public CharCase getCharCase() {
        return charCase;
    }

    public void setCharCase(CharCase charCase) {
        this.charCase = charCase;
    }

    public boolean isFuture() {
        return future;
    }

    public void setFuture(boolean future) {
        this.future = future;
    }

    public boolean isPast() {
        return past;
    }

    public void setPast(boolean past) {
        this.past = past;
    }
}
