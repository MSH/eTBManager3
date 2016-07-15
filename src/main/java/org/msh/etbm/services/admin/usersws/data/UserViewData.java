package org.msh.etbm.services.admin.usersws.data;

import org.msh.etbm.db.enums.UserView;

import java.util.Optional;
import java.util.UUID;

/**
 * Store information about the user view restriction. Used for creation and updating of the user
 * data by the services
 * <p>
 * Created by rmemoria on 10/2/16.
 */
public class UserViewData {
    /**
     * The user view
     */
    private Optional<UserView> view;

    /**
     * The administrative unit ID, case view is {@link UserView#ADMINUNIT}
     */
    private Optional<UUID> adminUnitId;

    /**
     * No argument constructor
     */
    public UserViewData() {
        super();
    }

    public UserViewData(UserView view) {
        this.view = Optional.of(view);
    }

    /**
     * Constructor passing the arguments
     *
     * @param view        the user view
     * @param adminUnitId
     */
    public UserViewData(UserView view, UUID adminUnitId) {
        this.view = Optional.of(view);
        this.adminUnitId = Optional.of(adminUnitId);
    }

    @Override
    public String toString() {
        String s = view != null ? view.toString() : "view = null";

        if (adminUnitId != null) {
            s += ", " + adminUnitId.toString();
        }

        return s;
    }

    public Optional<UserView> getView() {
        return view;
    }

    public void setView(Optional<UserView> view) {
        this.view = view;
    }

    public Optional<UUID> getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(Optional<UUID> adminUnitId) {
        this.adminUnitId = adminUnitId;
    }
}
