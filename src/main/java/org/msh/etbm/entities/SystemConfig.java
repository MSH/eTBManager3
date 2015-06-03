package org.msh.etbm.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Store e-TB Manager configuration information. Id is always = 1
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="systemconfig")
public class SystemConfig {

	@Id
	private Integer id;

	@Column(length=100)
	@NotNull
	private String systemURL;
	
	@Column(length=200)
	private String pageRootURL;

	@Column(length=100)
	@NotNull
	private String systemMail;
	
	private boolean allowRegPage;
	
	@ManyToOne
	@JoinColumn(name="WORKSPACE_ID")
	private Workspace workspace;
	
	@ManyToOne
	@JoinColumn(name="USERPROFILE_ID")
	private UserProfile userProfile;
	
	@ManyToOne
	@JoinColumn(name="TBUNIT_ID")
	private Tbunit tbunit;
	
	@Column(length=100)
	private String adminMail;

	// used to control system version number and database version number
	private Integer buildVersion;
	
	private Integer buildNumber;

    @Lob
    private String otherLinks;

    @Column(length = 250)
    private String updateSite;

    @Column(length = 250)
    private String jbossPath;

    @ManyToOne
    @JoinColumn(name="PUBDS_WORKSPACE_ID")
    private Workspace pubDashboardWorkspace;

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
	 * @return the systemMail
	 */
	public String getSystemMail() {
		return systemMail;
	}

	/**
	 * @param systemMail the systemMail to set
	 */
	public void setSystemMail(String systemMail) {
		this.systemMail = systemMail;
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

	/**
	 * @return the tbunit
	 */
	public Tbunit getTbunit() {
		return tbunit;
	}

	/**
	 * @param tbunit the tbunit to set
	 */
	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
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

/*
	@Override
	public String toString() {
		return Messages.instance().get("admin.syssetup");
	}
*/

	/**
	 * @return the buildVersion
	 */
	public Integer getBuildVersion() {
		return buildVersion;
	}

	/**
	 * @param buildVersion the buildVersion to set
	 */
	public void setBuildVersion(Integer buildVersion) {
		this.buildVersion = buildVersion;
	}

	/**
	 * @return the buildNumber
	 */
	public Integer getBuildNumber() {
		return buildNumber;
	}

	/**
	 * @param buildNumber the buildNumber to set
	 */
	public void setBuildNumber(Integer buildNumber) {
		this.buildNumber = buildNumber;
	}

	/**
	 * @return the pageRootURL
	 */
	public String getPageRootURL() {
		return pageRootURL;
	}

	/**
	 * @param pageRootURL the pageRootURL to set
	 */
	public void setPageRootURL(String pageRootURL) {
		this.pageRootURL = pageRootURL;
	}

    public String getOtherLinks() {
        return otherLinks;
    }

    public void setOtherLinks(String otherLinks) {
        this.otherLinks = otherLinks;
    }

    public String getUpdateSite() {
        return updateSite;
    }

    public void setUpdateSite(String updateSite) {
        this.updateSite = updateSite;
    }

    public String getJbossPath() {
        return jbossPath;
    }

    public void setJbossPath(String jbossPath) {
        this.jbossPath = jbossPath;
    }

    public Workspace getPubDashboardWorkspace() {
        return pubDashboardWorkspace;
    }

    public void setPubDashboardWorkspace(Workspace pubDashboardWorkspace) {
        this.pubDashboardWorkspace = pubDashboardWorkspace;
    }
}
