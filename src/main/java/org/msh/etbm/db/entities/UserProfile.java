package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;
import org.msh.etbm.db.enums.CaseClassification;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="userprofile")
public class UserProfile extends WSObject implements Serializable, Comparable<UserProfile> {
	private static final long serialVersionUID = -1798192936426485144L;

	@Column(length=100)
	@NotNull
	@PropertyLog(messageKey="form.name")
	private String name;

	@OneToMany(fetch= FetchType.LAZY, mappedBy="userProfile",cascade={CascadeType.ALL})
    private List<UserPermission> permissions = new ArrayList<UserPermission>();

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String legacyId;


	public UserPermission permissionByRole(UserRole role, CaseClassification classif) {
		for (UserPermission up: getPermissions()) {
			if (up.getUserRole().equals(role)) {
				if ((classif == null) || ((classif != null) && (classif == up.getCaseClassification())))
					return up;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int compareTo(UserProfile userProfile) {
		return name.compareTo(userProfile.getName());
	}

	public List<UserPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserPermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @param legacyId the legacyId to set
	 */
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}

	/**
	 * @return the legacyId
	 */
	public String getLegacyId() {
		return legacyId;
	}
}
