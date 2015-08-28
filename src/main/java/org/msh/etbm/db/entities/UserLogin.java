/*
 * UserLogin.java
 *
 * Created on 29 de Janeiro de 2007, 13:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Ricardo
 */

@Entity
@Table(name="userlogin")
public class UserLogin {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="USER_ID")
	@NotNull
    private User user;
    
    @Temporal(value = TemporalType.TIMESTAMP)
	@NotNull
    private Date loginDate;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date logoutDate;
    
    @Column(length=200)
    private String Application;
    
    @Column(length=16)
    private String IpAddress;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="WORKSPACE_ID")
	@NotNull
    private Workspace workspace;
    
    @Column(length=32)
    private String sessionId;


    public Workspace getDefaultWorkspace() {
    	return (user != null? getUser().getDefaultWorkspace().getWorkspace(): null);
    }

/*
    public String getDisplayLocale() {
    	return Locale.instance().getDisplayName(Locale.instance());
    }
*/

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getApplication() {
        return Application;
    }

    public void setApplication(String Application) {
        this.Application = Application;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String IpAddress) {
        this.IpAddress = IpAddress;
    }

	/**
	 * @param workspace the workspace to set
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
		if (workspace != null)
			workspace.getId();
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}