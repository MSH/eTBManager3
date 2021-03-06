package org.msh.etbm.db.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "userpermission")
public class UserPermission {

    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "uuid2", strategy = "uuid2", parameters = {@org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    private UUID id;

    @NotNull
    private String permission;

    @ManyToOne
    @JoinColumn(name = "PROFILE_ID")
    @NotNull
    private UserProfile userProfile;

    private boolean canChange;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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

}
