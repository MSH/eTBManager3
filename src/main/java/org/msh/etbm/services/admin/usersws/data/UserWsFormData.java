package org.msh.etbm.services.admin.usersws.data;

import org.msh.etbm.db.enums.UserState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Store information about a user workspace to be edited in a form and post back for updates
 * Created by rmemoria on 26/1/16.
 */
public class UserWsFormData {

    private Optional<String> name;
    private Optional<String> login;
    private Optional<String> email;
    private Optional<UUID> unitId;
    private Optional<UserState> state;
    private Optional<Boolean> administrator;
    private Optional<String> comments;
    private Optional<String> customId;
    private Optional<Boolean> playOtherUnits;
    private List<UUID> profiles;
    private UserViewData view;
    private Optional<Boolean> sendSystemMessages;


    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getLogin() {
        return login;
    }

    public void setLogin(Optional<String> login) {
        this.login = login;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<UUID> getUnitId() {
        return unitId;
    }

    public void setUnitId(Optional<UUID> unitId) {
        this.unitId = unitId;
    }

    public Optional<UserState> getState() {
        return state;
    }

    public void setState(Optional<UserState> state) {
        this.state = state;
    }

    public Optional<Boolean> getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Optional<Boolean> administrator) {
        this.administrator = administrator;
    }

    public Optional<String> getComments() {
        return comments;
    }

    public void setComments(Optional<String> comments) {
        this.comments = comments;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }

    public Optional<Boolean> getPlayOtherUnits() {
        return playOtherUnits;
    }

    public void setPlayOtherUnits(Optional<Boolean> playOtherUnits) {
        this.playOtherUnits = playOtherUnits;
    }

    public List<UUID> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<UUID> profiles) {
        this.profiles = profiles;
    }

    public UserViewData getView() {
        if (view == null) {
            view = new UserViewData();
        }
        return view;
    }

    public void setView(UserViewData view) {
        this.view = view;
    }

    public Optional<Boolean> getSendSystemMessages() {
        return sendSystemMessages;
    }

    public void setSendSystemMessages(Optional<Boolean> sendSystemMessages) {
        this.sendSystemMessages = sendSystemMessages;
    }
}
