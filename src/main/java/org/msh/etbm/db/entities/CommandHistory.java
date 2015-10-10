package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Store information about a command executed in the system
 *
 * Created by rmemoria on 9/10/15.
 */
public class CommandHistory {

    /**
     * The table primary key
     */
    @Id
    private UUID id;

    /**
     * The command type, example, 'user.create', 'case.treatment.start', 'medicine.remove'
     */
    @Column(length = 200)
    private String type;

    /**
     * The data in JSON format that was used to execute the command
     */
    @Lob
    private String data;

    /**
     * The date and time that the command was executed
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    /**
     * The user that executed the command
     */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="USERLOG_ID")
    @NotNull
    private UserLog user;

    /**
     * The workspace where the command was executed
     */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="WORKSPACELOG_ID")
    private WorkspaceLog workspace;

    /**
     * The unit related to the execution of the command. Its value is null
     * if the command was executed out of a unit scope
     */
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="UNIT_ID")
    private Tbunit unit;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public Tbunit getUnit() {
        return unit;
    }

    public void setUnit(Tbunit unit) {
        this.unit = unit;
    }
}
