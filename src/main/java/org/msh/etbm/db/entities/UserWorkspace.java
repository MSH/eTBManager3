package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.UserView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "userworkspace")
public class UserWorkspace extends WorkspaceEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIT_ID")
	@PropertyLog(operations = {Operation.NEW})
	private Unit unit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@NotNull
	@PropertyLog(ignore = true)
	private User user;

    /**
     * If true, user is granted all permissions in the system
     */
    private boolean administrator;
	

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "userworkspace_profiles",
            joinColumns = {@JoinColumn(name = "USERWORKSPACE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USERPROFILE_ID")})
    @PropertyLog(operations = {Operation.NEW})
	private List<UserProfile> profiles = new ArrayList<>();


	@Column(name = "USER_VIEW")
	@NotNull
	@PropertyLog(operations = {Operation.NEW})
	private UserView view;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADMINUNIT_ID")
	@PropertyLog(operations = {Operation.NEW})
    @NotNull
	private AdministrativeUnit adminUnit;
    
    private boolean playOtherUnits;


	/**
	 * Return the text to be displayed according to the view
	 * @return
	 */
/*
	public String getDisplayView() {
		switch (getView()) {
		case COUNTRY:
			return getWorkspace().getName().toString();
		case ADMINUNIT:
			return (getAdminUnit() != null? adminUnit.getCountryStructure().getName().toString() + ": " + adminUnit.getName().toString(): null);
		case TBUNIT:
			return Messages.instance().get("UserView.TBUNIT") + ": " + getTbunit().getName().toString();
		default:
			return null;
		}
	}
*/

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserView getView() {
		return view;
	}

	public void setView(UserView view) {
		this.view = view;
	}

	public boolean isPlayOtherUnits() {
		return playOtherUnits;
	}

	public void setPlayOtherUnits(boolean playOtherUnits) {
		this.playOtherUnits = playOtherUnits;
	}

	/**
	 * @param adminUnit the adminUnit to set
	 */
	public void setAdminUnit(AdministrativeUnit adminUnit) {
		this.adminUnit = adminUnit;
	}

	/**
	 * @return the adminUnit
	 */
	public AdministrativeUnit getAdminUnit() {
		return adminUnit;
	}

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public List<UserProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<UserProfile> profiles) {
        this.profiles = profiles;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    @Override
    public String getDisplayString() {
        return user.getDisplayString() + " - " + getWorkspace().getDisplayString();
    }

}
