package org.msh.etbm.services.admin.onlinereport;

import java.util.Date;

/**
 * Created by msantos on 9/3/16.
 */
public class OnlineUsersRepData {

    private String userLogin;
    private String userName;
    private Date loginDate;
    private Date lastAccess;

    public OnlineUsersRepData(String userLogin, String userName, Date loginDate, Date lastAccess) {
        this.userLogin = userLogin;
        this.userName = userName;
        this.loginDate = loginDate;
        this.lastAccess = lastAccess;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }
}
