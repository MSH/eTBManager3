package org.msh.etbm.commons.entities;

/**
 * Exception generated inside an entity to indicate that an error occured and operation
 * cannot continue
 *
 * Created by rmemoria on 28/10/15.
 */
public class EntityValidationException extends Exception {
    private String field;
    private String group;

    public EntityValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public EntityValidationException(String field, String message, String group) {
        super(message);
        this.field = field;
        this.group = group;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
