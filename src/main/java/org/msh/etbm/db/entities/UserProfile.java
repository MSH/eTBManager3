package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.Displayable;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="userprofile")
public class UserProfile extends WorkspaceData implements Displayable {

	@Column(length=100)
	@NotNull
	@PropertyLog(messageKey="form.name")
	private String name;

	@OneToMany(fetch= FetchType.LAZY, mappedBy="userProfile",cascade={CascadeType.ALL})
    private List<UserPermission> permissions = new ArrayList<UserPermission>();

	@Column(length=50)
	@PropertyLog(messageKey="form.customId")
	private String customId;


	public UserPermission permissionByRole(UserRole role) {
		for (UserPermission up: getPermissions()) {
			if (up.getUserRole().equals(role)) {
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

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public String getDisplayString() {
        return name;
    }
}
