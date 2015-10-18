package org.msh.etbm.commons.commands;

import org.msh.etbm.commons.commands.data.CommandData;
import org.msh.etbm.commons.commands.data.DiffLogData;
import org.msh.etbm.commons.commands.data.ListLogData;
import org.msh.etbm.commons.commands.data.TextLogData;
import org.msh.etbm.commons.commands.impl.CommandException;
import org.msh.etbm.db.dto.UnitDTO;
import org.msh.etbm.db.dto.UserDTO;
import org.msh.etbm.db.dto.WorkspaceDTO;

import java.util.UUID;

/**
 * Data to be used to save a command in the command history
 * Created by rmemoria on 17/10/15.
 */
public class CommandHistoryInput {
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
     * The data to be stored by the command history
     */
    private CommandData data;

    private WorkspaceDTO workspace;

    private UserDTO user;

    private UnitDTO unit;


    /**
     * Initialize the detail log to store a list of key, previous value and new value
     * @return
     */
    public DiffLogData beginDiffLog() {
        checkDataClass(ListLogData.class);
        return (DiffLogData)data;
    }

    /**
     * Initialize the detail log to store a list of values
     * @return
     */
    public ListLogData beginListLog() {
        checkDataClass(ListLogData.class);
        return (ListLogData)data;
    }

    /**
     * Initialize the detail log to store a text
     * @return
     */
    public TextLogData beginTextLog() {
        checkDataClass(TextLogData.class);
        return (TextLogData)data;
    }

    /**
     * Check if data was already created with the given data class
     * @param clazz the class to check from
     */
    private void checkDataClass(Class clazz) {
        if (data != null && clazz.isAssignableFrom(data.getClass())) {
            throw new CommandException("Log details was already initialized");
        }
        else {
            try {
                data = (CommandData)clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new CommandException(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new CommandException(e);
            }
        }
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

    public CommandData getData() {
        return data;
    }

    public void setData(CommandData data) {
        this.data = data;
    }

    public WorkspaceDTO getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceDTO workspace) {
        this.workspace = workspace;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UnitDTO getUnit() {
        return unit;
    }

    public void setUnit(UnitDTO unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
