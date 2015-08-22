package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;
import org.msh.etbm.db.enums.UserView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="userworkspace")
public class UserWorkspace extends WSObject {

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="UNIT_ID")
	@PropertyLog(operations={Operation.NEW})
	private Tbunit tbunit;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="LABORATORY_ID")
    @PropertyLog(operations={Operation.NEW})
    private Laboratory laboratory;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	@NotNull
	@PropertyLog(ignore=true)
	private User user;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="PROFILE_ID")
	@NotNull
	@PropertyLog(operations={Operation.NEW})
	private UserProfile profile;
	
	@Column(name="USER_VIEW")
	@NotNull
	@PropertyLog(operations={Operation.NEW})
	private UserView view;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ADMINUNIT_ID")
	@PropertyLog(operations={Operation.NEW})
	private AdministrativeUnit adminUnit;
    
    private boolean playOtherUnits;

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof UserWorkspace))
			return false;

		return ((UserWorkspace)obj).getId().equals(getId());
	}

	
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

	public Tbunit getTbunit() {
		return tbunit;
	}

	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
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


    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }
}
