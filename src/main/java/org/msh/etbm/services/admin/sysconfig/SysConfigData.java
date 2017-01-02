package org.msh.etbm.services.admin.sysconfig;

import org.msh.etbm.commons.SynchronizableItem;

/**
 * Created by rmemoria on 23/11/16.
 */
public class SysConfigData {

    private String systemURL;
    private boolean allowRegPage;
    private SynchronizableItem workspace;
    private SynchronizableItem userProfile;
    private SynchronizableItem unit;
    private String adminMail;
    private String updateSite;
    private boolean ulaActive;
    private boolean clientMode;
    private String serverURL;
    private Integer version;

    public String getSystemURL() {
        return systemURL;
    }

    public void setSystemURL(String systemURL) {
        this.systemURL = systemURL;
    }

    public boolean isAllowRegPage() {
        return allowRegPage;
    }

    public void setAllowRegPage(boolean allowRegPage) {
        this.allowRegPage = allowRegPage;
    }

    public SynchronizableItem getWorkspace() {
        return workspace;
    }

    public void setWorkspace(SynchronizableItem workspace) {
        this.workspace = workspace;
    }

    public SynchronizableItem getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(SynchronizableItem userProfile) {
        this.userProfile = userProfile;
    }

    public SynchronizableItem getUnit() {
        return unit;
    }

    public void setUnit(SynchronizableItem unit) {
        this.unit = unit;
    }

    public String getAdminMail() {
        return adminMail;
    }

    public void setAdminMail(String adminMail) {
        this.adminMail = adminMail;
    }

    public String getUpdateSite() {
        return updateSite;
    }

    public void setUpdateSite(String updateSite) {
        this.updateSite = updateSite;
    }

    public boolean isUlaActive() {
        return ulaActive;
    }

    public void setUlaActive(boolean ulaActive) {
        this.ulaActive = ulaActive;
    }

    public boolean isClientMode() {
        return clientMode;
    }

    public void setClientMode(boolean clientMode) {
        this.clientMode = clientMode;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
