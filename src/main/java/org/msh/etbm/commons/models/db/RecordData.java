package org.msh.etbm.commons.models.db;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Expose CRUD operations to a model defined in the {@link org.msh.etbm.commons.models.data.Model} class.
 * <p/>
 * This object
 * Created by rmemoria on 12/7/16.
 */
public class RecordData {

    private UUID id;

    private Map<String, Object> values = new HashMap<>();

    public RecordData() {
        super();
    }

    public RecordData(UUID id, Map<String, Object> values) {
        this.id = id;
        this.values.putAll(values);
    }

    public RecordData(Map<String, Object> values) {
        this.values.putAll(values);
    }

    /**
     * Return the field value casted to a string
     * @param field the name of the field in the model
     * @return String value
     */
    public String getString(String field) {
        return (String)getValues().get(field);
    }

    /**
     * Return the field value as an integer
     * @param field the name of the field in the model
     * @return Integer value
     */
    public Integer getInteger(String field) {
        return (Integer)getValues().get(field);
    }

    /**
     * Return the field value as boolean
     * @param field the name of the field in the model
     * @return Boolean value
     */
    public Boolean getBoolean(String field) {
        return (Boolean)getValues().get(field);
    }

    /**
     * Return the field value as a double number
     * @param field the name of the field in the model
     * @return Double value or null if not found
     */
    public Double getDouble(String field) {
        return (Double)getValues().get(field);
    }

    /**
     * Return the field value as a Date
     * @param field the name of the field in the model
     * @return Date value or null if not found
     */
    public Date getDate(String field) {
        return (Date)getValues().get(field);
    }

    /**
     * Return the field value as an UUID type
     * @param field the name of the field in the model
     * @return UUID value or null if not found
     */
    public UUID getUUID(String field) {
        return (UUID)getValues().get(field);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        String s = id != null ? "id=" + id.toString() + " " : "NEW ";
        return s + values.toString();
    }
}
