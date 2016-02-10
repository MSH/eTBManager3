package org.msh.etbm.services.admin.usersws;

import org.msh.etbm.db.enums.UserView;

import java.util.UUID;

/**
 * Store information about the user view restriction. Used for creation and updating of the user
 * data by the services
 *
 * Created by rmemoria on 10/2/16.
 */
public class UserViewData {
    /**
     * The user view
     */
    private UserView view;

    /**
     * The administrative unit ID, case view is {@link UserView#ADMINUNIT}
     */
    private UUID adminUnitId;

    /**
     * Constructor passing the arguments
     * @param view the user view
     * @param adminUnitId
     */
    public UserViewData(UserView view, UUID adminUnitId) {
        this.view = view;
        this.adminUnitId = adminUnitId;
    }

    public UserViewData(UserView view) {
        this.view = view;
    }

    @Override
    public String toString() {
        String s = view != null? view.toString() : "view = null";

        if (adminUnitId != null) {
            s += ", " + adminUnitId.toString();
        }

        return s;
    }

    /**
     * No argument constructor
     */
    public UserViewData() {
        super();
    }

    public UserView getView() {
        return view;
    }

    public void setView(UserView view) {
        this.view = view;
    }

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }
}
