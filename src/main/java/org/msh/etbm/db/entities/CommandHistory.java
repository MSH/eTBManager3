package org.msh.etbm.db.entities;

import org.msh.etbm.commons.commands.CommandAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Store information about a command executed in the system
 *
 * Created by rmemoria on 9/10/15.
 */
@Entity
@Table(name = "commandhistory")
public class CommandHistory {

    /**
     * The table primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The command type, example, 'user.create', 'case.treatment.start', 'medicine.remove'
     */
    @Column(length = 200, name = "type")
    @NotNull
    private String type;

    @NotNull
    private CommandAction action;

    /**
     * The data in JSON format that was used to execute the command
     */
    @Lob
    private String data;

    /**
     * The date and time that the command was executed
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date execDate;

    /**
     * The user that executed the command
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERLOG_ID")
    @NotNull
    private UserLog user;

    /**
     * The workspace where the command was executed
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACELOG_ID")
    private WorkspaceLog workspace;

    /**
     * The unit related to the execution of the command. Its value is null
     * if the command was executed out of a unit scope
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_ID")
    private Unit unit;

    /**
     * The ID of the entity being affected by this command
     */
    @Column(length = 32)
    private UUID entityId;

    /**
     * The name of the entity being handled by the command
     */
    @Column(length = 250)
    private String entityName;

    /**
     * The ID of the parent entity (example, TbCase) being affected by this command
     */
    @Column(length = 32)
    private UUID parentId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getExecDate() {
        return execDate;
    }

    public void setExecDate(Date execDate) {
        this.execDate = execDate;
    }

    public UserLog getUser() {
        return user;
    }

    public void setUser(UserLog user) {
        this.user = user;
    }

    public WorkspaceLog getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceLog workspace) {
        this.workspace = workspace;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public CommandAction getAction() {
        return action;
    }

    public void setAction(CommandAction action) {
        this.action = action;
    }
}
