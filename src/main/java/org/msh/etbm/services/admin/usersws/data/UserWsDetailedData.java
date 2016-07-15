package org.msh.etbm.services.admin.usersws.data;

import org.msh.etbm.commons.SynchronizableItem;

import java.util.List;

/**
 * Created by rmemoria on 26/1/16.
 */
public class UserWsDetailedData extends UserWsData {
    private String login;
    private String email;
    private String comments;
    private String customId;
    private boolean sendSystemMessages;
    private boolean ulaAccepted;
    private boolean playOtherUnits;

    private List<SynchronizableItem> profiles;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public boolean isSendSystemMessages() {
        return sendSystemMessages;
    }

    public void setSendSystemMessages(boolean sendSystemMessages) {
        this.sendSystemMessages = sendSystemMessages;
    }

    public boolean isUlaAccepted() {
        return ulaAccepted;
    }

    public void setUlaAccepted(boolean ulaAccepted) {
        this.ulaAccepted = ulaAccepted;
    }

    public boolean isPlayOtherUnits() {
        return playOtherUnits;
    }

    public void setPlayOtherUnits(boolean playOtherUnits) {
        this.playOtherUnits = playOtherUnits;
    }

    public List<SynchronizableItem> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<SynchronizableItem> profiles) {
        this.profiles = profiles;
    }
}
