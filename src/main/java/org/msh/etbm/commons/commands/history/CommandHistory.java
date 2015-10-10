package org.msh.etbm.commons.commands.history;


import java.util.Date;

/**
 * Information about a command
 * Created by rmemoria on 7/10/15.
 */
public class CommandHistory {

    /**
     * The command type, i.e, user.create, medicine.edit, case.treatment.start
     */
    private String type;

    /**
     * The date and time the command was executed
     */
    private Date dateTime;

    /**
     * The data related to this command
     */
    private Object data;

    /**
     * The user that executed this command
     */
    private User user;

    /**
     * The unit that this command was executed into
     */
    private Unit unit;

    /**
     * The workspace where the command was executed into
     */
    private Workspace workspace;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
}
