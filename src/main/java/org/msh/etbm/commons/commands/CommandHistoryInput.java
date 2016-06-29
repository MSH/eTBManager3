package org.msh.etbm.commons.commands;

import org.msh.etbm.commons.commands.details.CommandLogDetail;
import org.msh.etbm.commons.commands.details.DetailWriter;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Data to be used to save a command in the command history
 * Created by Ricardo Memoria on 17/10/15.
 */
public class CommandHistoryInput {

    /**
     * The method source that generated the command
     */
    private Method method;

    /**
     * The type of the command to be stored
     */
    private String type;

    /**
     * The main entity ID being handled by the command
     */
    private UUID entityId;

    /**
     * The parent ID, in case there is a main entity where the entity ID depends on
     */
    private UUID parentId;

    /**
     * The action being handled by the command
     */
    private CommandAction action;

    /**
     * The entity name involved in the command
     */
    private String entityName;

    /**
     * The details about the command
     */
    private DetailWriter detail;

    private UUID workspaceId;

    private UUID userId;

    private UUID unitId;

    /**
     * If true, command log registration is canceled
     */
    private boolean canceled;


    /**
     * Cancel the log registration. This operation is called by the log handler, if, for some specific
     * reason, the log should not be recorded (for example, nothing was done)
     */
    public void cancelLog() {
        canceled = true;
    }


    public CommandHistoryInput setDetailText(String text) {
        getDetail().setText(text);
        return this;
    }


    public CommandHistoryInput addItem(String text, Object value) {
        getDetail().addItem(text, value);
        return this;
    }

    public CommandHistoryInput addDiff(String text, Object oldValue, Object newValue) {
        getDetail().addDiff(text, oldValue, newValue);
        return this;
    }

    /**
     * Return the detail writer object in use internally by the system
     *
     * @return instance of DetailWriter class
     */
    private DetailWriter getDetail() {
        if (detail == null) {
            detail = new DetailWriter();
        }
        return detail;
    }

    /**
     * Return the detail data stored in the command
     *
     * @return
     */
    public CommandLogDetail getDetailData() {
        return detail != null ? detail.getDetail() : null;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method source) {
        this.method = source;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public CommandAction getAction() {
        return action;
    }

    public void setAction(CommandAction action) {
        this.action = action;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }


    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
