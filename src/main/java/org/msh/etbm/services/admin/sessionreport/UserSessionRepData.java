package org.msh.etbm.services.admin.sessionreport;

import java.util.Date;

/**
 * Created by msantos on 9/3/16.
 */
public class UserSessionRepData {

    private String userLogin;
    private String userName;
    private Date loginDate;
    private Date logoutDate;
    private String ipAddress;
    private String application;

    public UserSessionRepData(String userLogin, String userName, Date loginDate, Date logoutDate, String ipAddress, String application) {
        this.userLogin = userLogin;
        this.userName = userName;
        this.loginDate = loginDate;
        this.logoutDate = logoutDate;
        this.ipAddress = ipAddress;
        this.application = application;
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

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
