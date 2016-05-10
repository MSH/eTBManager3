package org.msh.etbm.services.admin.sysconfig;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by rmemoria on 6/4/16.
 */
public class SysConfigFormData {

    private Optional<String> systemURL;

    private Optional<String> pageRootURL;

    private Optional<String> adminMail;

    private Optional<Boolean> allowRegPage;

    private Optional<Boolean> ulaActive;

    private Optional<UUID> workspace;

    private Optional<UUID> unit;

    private Optional<UUID> userProfile;


    public Optional<String> getSystemURL() {
        return systemURL;
    }

    public void setSystemURL(Optional<String> systemURL) {
        this.systemURL = systemURL;
    }

    public Optional<String> getPageRootURL() {
        return pageRootURL;
    }

    public void setPageRootURL(Optional<String> pageRootURL) {
        this.pageRootURL = pageRootURL;
    }

    public Optional<String> getAdminMail() {
        return adminMail;
    }

    public void setAdminMail(Optional<String> adminMail) {
        this.adminMail = adminMail;
    }

    public Optional<Boolean> getAllowRegPage() {
        return allowRegPage;
    }

    public void setAllowRegPage(Optional<Boolean> allowRegPage) {
        this.allowRegPage = allowRegPage;
    }

    public Optional<UUID> getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Optional<UUID> workspace) {
        this.workspace = workspace;
    }

    public Optional<UUID> getUnit() {
        return unit;
    }

    public void setUnit(Optional<UUID> unit) {
        this.unit = unit;
    }

    public Optional<Boolean> getUlaActive() {
        return ulaActive;
    }

    public void setUlaActive(Optional<Boolean> ulaActive) {
        this.ulaActive = ulaActive;
    }

    public Optional<UUID> getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Optional<UUID> userProfile) {
        this.userProfile = userProfile;
    }
}
