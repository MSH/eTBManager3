package org.msh.etbm.db.entities;

import javax.persistence.*;

/**
 * Store e-TB Manager configuration information. Id is always = 1
 *
 * @author Ricardo Memoria
 */
@Entity
@Table(name = "systemconfig")
public class SystemConfig {

    public static final int PRIMARY_KEY = 1;

    @Id
    private Integer id;

    /**
     * The system URL, used mostly in e-mail messages to point to the e-TB Manager address
     */
    @Column(length = 250)
    private String systemURL;

    /**
     * If true, a link to user self registration will be displayed in the login page
     */
    private boolean allowRegPage;

    /**
     * The user workspace, if allowRegPage is true
     */
    @ManyToOne
    @JoinColumn(name = "WORKSPACE_ID")
    private Workspace workspace;

    /**
     * The user user profile, if allowRegPage is true
     */
    @ManyToOne
    @JoinColumn(name = "USERPROFILE_ID")
    private UserProfile userProfile;

    /**
     * The user unit, if allowRegPage is true
     */
    @ManyToOne
    @JoinColumn(name = "UNIT_ID")
    private Unit unit;

    /**
     * The administrator e-mail address, used by the system to send e-mail messages in case of errors
     */
    @Column(length = 100)
    private String adminMail;

    /**
     * The URL of the update site, if available
     */
    @Column(length = 250)
    private String updateSite;

    /**
     * The workspace of the public dashboard, mostly used when embedding the dashboard in a web page
     * for public access
     */
    @ManyToOne
    @JoinColumn(name = "PUBDS_WORKSPACE_ID")
    private Workspace pubDashboardWorkspace;

    /**
     * If true, the ULA will be displayed once to the user to be accepted
     */
    private boolean ulaActive;

    /**
     * If true, this instance is a client instance that synchronizes with a server
     */
    private boolean clientMode;

    /**
     * ONLY FOR CLIENT INSTANCE OF ETB-MANAGER
     * The URL of the server instance
     */
    @Column(length = 250)
    private String serverURL;

    /**
     * ONLY FOR CLIENT INSTANCE OF ETB-MANAGER
     * The last sync file version imported by a client mode instance
     */
    private Integer version;

    /**
     * ONLY FOR CLIENT INSTANCE OF ETB-MANAGER
     * The unit id of synchronized unit
     */
    @ManyToOne
    @JoinColumn(name = "SYNC_UNIT_ID")
    private Unit syncUnit;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the systemURL
     */
    public String getSystemURL() {
        return systemURL;
    }

    /**
     * @param systemURL the systemURL to set
     */
    public void setSystemURL(String systemURL) {
        this.systemURL = systemURL;
    }

    /**
     * @return the allowRegPage
     */
    public boolean isAllowRegPage() {
        return allowRegPage;
    }

    /**
     * @param allowRegPage the allowRegPage to set
     */
    public void setAllowRegPage(boolean allowRegPage) {
        this.allowRegPage = allowRegPage;
    }

    /**
     * @return the workspace
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * @param workspace the workspace to set
     */
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    /**
     * @return the userProfile
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * @param userProfile the userProfile to set
     */
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * @return the adminMail
     */
    public String getAdminMail() {
        return adminMail;
    }

    /**
     * @param adminMail the adminMail to set
     */
    public void setAdminMail(String adminMail) {
        this.adminMail = adminMail;
    }


    public String getUpdateSite() {
        return updateSite;
    }

    public void setUpdateSite(String updateSite) {
        this.updateSite = updateSite;
    }

    public Workspace getPubDashboardWorkspace() {
        return pubDashboardWorkspace;
    }

    public void setPubDashboardWorkspace(Workspace pubDashboardWorkspace) {
        this.pubDashboardWorkspace = pubDashboardWorkspace;
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

    public Unit getSyncUnit() {
        return syncUnit;
    }

    public void setSyncUnit(Unit syncUnit) {
        this.syncUnit = syncUnit;
    }
}
