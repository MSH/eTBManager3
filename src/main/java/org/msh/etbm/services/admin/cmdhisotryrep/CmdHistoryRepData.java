package org.msh.etbm.services.admin.cmdhisotryrep;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.details.CommandLogDetail;

import java.util.Date;

/**
 * Created by msantos on 11/3/16.
 */
public class CmdHistoryRepData {

    private String type;
    private Item<CommandAction> action;
    private Date execDate;
    private String entityName;
    private String userName;
    private String unitName;
    private String adminUnitName;
    private CommandLogDetail detail;

    public CmdHistoryRepData(String type, Item<CommandAction> action, Date execDate, String entityName, String userName, String unitName, String adminUnitName, CommandLogDetail detail) {
        this.type = type;
        this.action = action;
        this.execDate = execDate;
        this.entityName = entityName;
        this.userName = userName;
        this.unitName = unitName;
        this.adminUnitName = adminUnitName;
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item<CommandAction> getAction() {
        return action;
    }

    public void setAction(Item<CommandAction> action) {
        this.action = action;
    }

    public Date getExecDate() {
        return execDate;
    }

    public void setExecDate(Date execDate) {
        this.execDate = execDate;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAdminUnitName() {
        return adminUnitName;
    }

    public void setAdminUnitName(String adminUnitName) {
        this.adminUnitName = adminUnitName;
    }

    public CommandLogDetail getDetail() {
        return detail;
    }

    public void setDetail(CommandLogDetail detail) {
        this.detail = detail;
    }
}
