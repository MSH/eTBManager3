package org.msh.etbm.db.entities;

import javax.persistence.*;

/**
 * Store e-TB Manager configuration information. Id is always = 1
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name = "systemconfig")
public class SystemConfig {

    public static final int PRIMARY_KEY = 1;

	@Id
	private Integer id;

	@Column(length = 100)
	private String systemURL;
	
	@Column(length = 200)
	private String pageRootURL;
	
	private boolean allowRegPage;
	
	@ManyToOne
	@JoinColumn(name = "WORKSPACE_ID")
	private Workspace workspace;
	
	@ManyToOne
	@JoinColumn(name = "USERPROFILE_ID")
	private UserProfile userProfile;
	
	@ManyToOne
	@JoinColumn(name = "UNIT_ID")
	private Unit unit;
	
	@Column(length = 100)
	private String adminMail;


    @Column(length = 250)
    private String updateSite;

    @ManyToOne
    @JoinColumn(name = "PUBDS_WORKSPACE_ID")
    private Workspace pubDashboardWorkspace;

    /**
     * If true, the ULA will be displayed once to the user to be accepted
     */
    private boolean ulaActive;

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
}
