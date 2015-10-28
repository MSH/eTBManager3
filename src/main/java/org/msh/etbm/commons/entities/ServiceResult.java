package org.msh.etbm.commons.entities;

import org.msh.etbm.commons.messages.MessageList;

import java.util.UUID;

/**
 * Created by rmemoria on 25/10/15.
 */
public class ServiceResult {

    private UUID id;
    private String entityName;
    private Class entityClass;
    private Diffs logDiffs;
    private ObjectValues logValues;
    private MessageList validationErrors;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String name) {
        this.entityName = name;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public Diffs getLogDiffs() {
        return logDiffs;
    }

    public void setLogDiffs(Diffs logDiffs) {
        this.logDiffs = logDiffs;
    }

    public ObjectValues getLogValues() {
        return logValues;
    }

    public void setLogValues(ObjectValues logValues) {
        this.logValues = logValues;
    }

    public MessageList getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(MessageList validationErrors) {
        this.validationErrors = validationErrors;
    }
}
