package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="userpermission")
public class UserPermission implements Serializable, Comparable<UserPermission> {
	private static final long serialVersionUID = 7565244271956307412L;

	@Id
    private UUID id;

	@ManyToOne
	@JoinColumn(name="ROLE_ID")
	@NotNull
	private UserRole userRole;

	@ManyToOne
	@JoinColumn(name="PROFILE_ID")
	@NotNull
	private UserProfile userProfile;

	private boolean canExecute;
	private boolean canChange;


/*
	@Override
	public String toString() {
		Map<String, String> msgs = Messages.instance();

		String s = "";
		if (!userRole.getName().equals("-"))
			s = userRole.getDisplayName();
		
		if ((caseClassification != null) && (userRole.isByCaseClassification())) {
			s = msgs.get(caseClassification.getKey()) + " - " + s;
		}
		return s;
	}
*/

	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public int compareTo(UserPermission userPermission) {
		return (((getUserRole() != null) && (userPermission.getUserRole() != null))? 
				userRole.compareTo(userPermission.getUserRole()): 
				0);
	}

	/**
	 * @return the canChange
	 */
	public boolean isCanChange() {
		return canChange;
	}

	/**
	 * @param canChange the canChange to set
	 */
	public void setCanChange(boolean canChange) {
		this.canChange = canChange;
	}


	/**
	 * @return the canExecute
	 */
	public boolean isCanExecute() {
		return canExecute;
	}


	/**
	 * @param canExecute the canExecute to set
	 */
	public void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

}
