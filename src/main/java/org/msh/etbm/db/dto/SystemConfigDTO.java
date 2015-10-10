package org.msh.etbm.db.dto;

/**
 * Created by rmemoria on 5/10/15.
 */
public class SystemConfigDTO {

    private String systemURL;

    private String pageRootURL;

    private String systemMail;

    private boolean allowRegPage;

    private WorkspaceDTO workspace;


    private UnitDTO unit;

    private String adminMail;

    private String updateSite;

    private WorkspaceDTO pubDashboardWorkspace;

    /**
     * If true, the ULA will be displayed once to the user to be accepted
     */
    private boolean ulaActive;

    public String getSystemURL() {
        return systemURL;
    }

    public void setSystemURL(String systemURL) {
        this.systemURL = systemURL;
    }

    public String getPageRootURL() {
        return pageRootURL;
    }

    public void setPageRootURL(String pageRootURL) {
        this.pageRootURL = pageRootURL;
    }

    public String getSystemMail() {
        return systemMail;
    }

    public void setSystemMail(String systemMail) {
        this.systemMail = systemMail;
    }

    public boolean isAllowRegPage() {
        return allowRegPage;
    }

    public void setAllowRegPage(boolean allowRegPage) {
        this.allowRegPage = allowRegPage;
    }

    public WorkspaceDTO getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceDTO workspace) {
        this.workspace = workspace;
    }

    public UnitDTO getUnit() {
        return unit;
    }

    public void setUnit(UnitDTO unit) {
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

    public WorkspaceDTO getPubDashboardWorkspace() {
        return pubDashboardWorkspace;
    }

    public void setPubDashboardWorkspace(WorkspaceDTO pubDashboardWorkspace) {
        this.pubDashboardWorkspace = pubDashboardWorkspace;
    }

    public boolean isUlaActive() {
        return ulaActive;
    }

    public void setUlaActive(boolean ulaActive) {
        this.ulaActive = ulaActive;
    }
}
